FROM node:14 AS builder
WORKDIR /app
COPY . .
RUN npm install && npm run-script build

FROM nginx:1.21.5
WORKDIR /usr/share/nginx/html
RUN rm -rf ./*
COPY --from=builder /app/build .

ENTRYPOINT ["nginx", "-g", "daemon off;"]
