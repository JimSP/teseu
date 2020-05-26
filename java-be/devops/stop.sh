#!/bin/sh

if [ -f batch.pid ]
then
	kill $(cat batch.pid)
fi
