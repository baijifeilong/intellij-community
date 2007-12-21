package com.intellij.psi.impl;

import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;

/**
 *
 */
public class CheckUtil {

  public static void checkWritable(PsiElement element) throws IncorrectOperationException{
    if (!element.isWritable()){
      if (element instanceof PsiDirectory){
        throw new IncorrectOperationException(
          PsiBundle.message("cannot.modify.a.read.only.directory", ((PsiDirectory)element).getVirtualFile().getPresentableUrl()));
      }
      else{
        PsiFile file = element.getContainingFile();
        if (file == null){
          throw new IncorrectOperationException();
        }
        final VirtualFile virtualFile = file.getVirtualFile();
        if (virtualFile == null){
          throw new IncorrectOperationException();
        }
        throw new IncorrectOperationException(PsiBundle.message("cannot.modify.a.read.only.file", virtualFile.getPresentableUrl()));
      }
    }
  }

  public static void checkDelete(VirtualFile file) throws IncorrectOperationException{
    if (FileTypeManager.getInstance().isFileIgnored(file.getName())) return;
    if (!file.isWritable()) {
      throw new IncorrectOperationException(PsiBundle.message("cannot.delete.a.read.only.file", file.getPresentableUrl()));
    }
    if (file.isDirectory()){
      VirtualFile[] children = file.getChildren();
      for (VirtualFile aChildren : children) {
        checkDelete(aChildren);
      }
    }
  }
}
