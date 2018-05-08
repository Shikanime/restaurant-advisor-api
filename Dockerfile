FROM gradle:alpine

WORKDIR /usr/src/app

ADD --chown=gradle . .

CMD ["gradle", "--stacktrace", "run"]
