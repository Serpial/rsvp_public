FROM node:22-alpine
LABEL org.opencontainers.image.authors="hutchison.dev"
ARG RSVP_ENV=test
WORKDIR /rsvp-ui/
COPY public/ /rsvp-ui/public
COPY src/ /rsvp-ui/src
COPY environment /rsvp-ui/environment
COPY package.json vite.config.js index.html /rsvp-ui/
RUN npm install && npm i -g serve && npm run build -- --mode=$RSVP_ENV
EXPOSE 3000
CMD [ "serve", "-s", "dist" ]