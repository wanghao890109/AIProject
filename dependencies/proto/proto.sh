#!/bin/zsh
# shellcheck disable=SC2006
gitURL=$1
currentPath=`pwd`

localProtoPath="${currentPath}/proto/"

if [ ! -d "$localProtoPath" ] ; then
    git clone $gitURL $localProtoPath
    echo "clone 代码"
else
    cd "$localProtoPath"
    git pull $gitUrl
    echo "pull 代码 $gitUrl"
fi

copyFrom="$localProtoPath"
copyTo="${currentPath}/file/"

echo "当前目录 ${currentPath}"

cd ${localProtoPath}

git config pull.rebase false

#git pull origin develop
git pull

echo "拉取代码完成"

cd ${currentPath}

rm  -rf "${copyTo}"

echo "删除完成"

mkdir "${copyTo}"

echo "目录"

cp -rf $copyFrom*.proto $copyTo

echo "拷贝完成"
