#!/bin/bash
read -p "please input:" TEST_STR
TEST_STR=$(echo $TEST_STR | sed "s/'//g")
echo $TEST_STR
