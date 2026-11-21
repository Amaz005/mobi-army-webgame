# Browser ↔ game server relay (spec 002-web-client, research.md R5).
# Binary WebSocket frames in, raw TCP bytes out, to ONE fixed destination: the game server.
FROM python:3.12-slim
RUN pip install --no-cache-dir websockify==0.13.0
EXPOSE 6080
USER nobody
ENTRYPOINT ["websockify", "0.0.0.0:6080", "server:19149"]
