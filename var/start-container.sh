#!/usr/bin/env bash
set -e
docker run \
  --rm \
  --publish 8080:8080 \
  goatfryed/spring-boot-diagnostic:develop