# Browser client (spec 002-web-client). Build context: the repository root, because the build reads the
# ORIGINAL client bytecode and resources from Army165local/ (never modified).

FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /src/web-client
COPY web-client/pom.xml ./
RUN mvn -B -q dependency:go-offline
COPY Army165local /src/Army165local
COPY web-client/src ./src
# Compiles the MIDP web layer, runs the JUnit tests, fixes getGameAction in the original bytecode, runs TeaVM.
RUN mvn -B -q package
# FR-006: the page shell must not load anything from another site.
RUN if grep -nE "(src|href)=[\"']?https?://|url\(['\"]?https?://|import .*https?://|\"(src|start_url)\"[[:space:]]*:[[:space:]]*\"https?://" \
        target/webapp/index.html target/webapp/play.css target/webapp/keypad.js \
        target/webapp/manifest.webmanifest; then \
      echo "off-site reference in page shell (FR-006)"; exit 1; fi

FROM nginx:1.27-alpine
COPY --from=build /src/web-client/target/webapp /usr/share/nginx/html
COPY docker/web/10-check-public.sh /docker-entrypoint.d/10-check-public.sh
RUN chmod +x /docker-entrypoint.d/10-check-public.sh
