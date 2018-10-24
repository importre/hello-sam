#!/usr/bin/env zsh

./gradlew clean shadowJar

sam package \
    --template-file template.yaml \
    --output-template-file packaged.yaml \
    --s3-bucket riiid-dev-hello

sam deploy \
    --template-file packaged.yaml \
    --stack-name hello-sam \
    --capabilities CAPABILITY_IAM
