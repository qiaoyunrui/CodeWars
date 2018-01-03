#!/usr/bin/env bash

set -eu
red=$(tput setaf 1)
green=$(tput setaf 2)

routerUrl="https://github.com/qiaoyunrui/Router.git"

installRouter() {
    if test -e "router"
    then
        echo "${red}Router has been installed!!${red}"
        return 1
    else
        git clone ${routerUrl}
        result=$?
        if [ $? -eq 0 ]
        then
            echo "include ':router:router-compiler'" >> settings.gradle
            echo "include ':router:router-annotation'" >> settings.gradle
            echo "include ':router:router-api'" >> settings.gradle
            echo "${green}Done!!${green}"
            return
        else
            echo "${red}Clone failed!!${red}"
            return
        fi
    fi
}

if [ $# -eq 0 ]
then
echo "${red}Invalid parameter!!${red}"
exit
fi

case $1 in
"router")
installRouter
;;
*)
echo "${red}Invalid parameter!!${red}"
;;
esac
