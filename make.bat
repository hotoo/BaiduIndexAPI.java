@echo off
REM chcp 936
REM chcp 65001

set fname=demo.html
echo ^<!DOCTYPE html^> > %fname%
echo ^<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN"^> >> %fname%
echo ^<head^> >> %fname%
echo ^<meta http-equiv="Content-Type" content="text/html; charset=gbk" /^> >> %fname%
echo ^<meta name="keywords" content="" /^> >> %fname%
echo ^<meta name="description" content="" /^> >> %fname%
echo ^<meta name="generator" content="gvim" /^> >> %fname%
echo ^<meta name="author" content="hotoo(hotoo.cn[AT]gmail.com)" /^> >> %fname%
echo ^<link rel="icon" href="favicon.ico" type="image/x-icon" /^> >> %fname%
echo ^<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" /^> >> %fname%
echo ^<link rel="stylesheet" type="text/css" href="baidu.index.css" media="all" /^> >> %fname%
echo ^<title^>Page Title^</title^> >> %fname%
echo ^</head^> >> %fname%
echo ^<body^> >> %fname%

REM D:
REM cd D:\workbench\BaiduIndexAPI.java
java Robot zhifubao >> %fname%

echo ^</body^> >> %fname%
echo ^</html^> >> %fname%
REM pause
