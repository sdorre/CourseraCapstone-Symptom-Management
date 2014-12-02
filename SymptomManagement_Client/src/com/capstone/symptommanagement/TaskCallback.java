package com.capstone.symptommanagement;

/* 
**
** Copyright 2014, Jules White
**
** 
*/

public interface TaskCallback<T> {

    public void success(T result);

    public void error(Exception e);

}
