FROM openjdk:alpine

WORKDIR /usr/src/app

COPY . .

CMD ['echo', 'lol']
