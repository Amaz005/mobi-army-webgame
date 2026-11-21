#!/bin/sh
# Public mode must have a domain and certificate files; otherwise only the web service refuses to start.
set -eu
if [ "${WEB_MODE:-local}" = "public" ]; then
  if [ -z "${WEB_DOMAIN:-}" ]; then
    echo "WEB_MODE=public needs WEB_DOMAIN in .env" >&2
    exit 1
  fi
  for f in /etc/nginx/certs/fullchain.pem /etc/nginx/certs/privkey.pem; do
    if [ ! -s "$f" ]; then
      echo "WEB_MODE=public needs $f (put it in docker/web/certs/)" >&2
      exit 1
    fi
  done
fi
