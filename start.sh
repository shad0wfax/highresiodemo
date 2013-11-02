#! /usr/bin/env sh
play -Dconfig.resource=prod.conf -DapplyEvolutions.default=true -Dhttp.port=9000 start &
