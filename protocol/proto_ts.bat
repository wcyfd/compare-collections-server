﻿
set SOURCE_DIR=doc
set TARGET_DIR=out\
mkdir %TARGET_DIR%
echo off
rem proto_path 是proto文件的import目录
echo on
rd /s /q ..\src\com\randioo\mahjong_server\protocol
protoc.exe --plugin=protoc-gen-as3="protoc-gen-ts.bat" --proto_path=%SOURCE_DIR% --as3_out=%TARGET_DIR% ./%SOURCE_DIR%/*.proto