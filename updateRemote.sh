#!/bin/bash

mvn package -P swagger
cd /home/t/Desktop/gitDMC/dmcdeploy/azure/localUpdate
./updateStackRestFromLocal.sh
