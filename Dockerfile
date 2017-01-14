FROM openjdk:jre-alpine

MAINTAINER nshou <nshou@coronocoya.net>

ENV MIRROR=http://apache.mirrors.pair.com \
    VERSION=3.4.9 \
    ES_VERSION=5.1.1 \
    KIBANA_VERSION=5.1.1

RUN apk add --quiet --no-progress --no-cache nodejs wget \
 && adduser -D elasticsearch

USER elasticsearch

WORKDIR /home/elasticsearch

RUN wget -q -O - https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-${ES_VERSION}.tar.gz \
 |  tar -zx \
 && mv elasticsearch-${ES_VERSION} elasticsearch \
 && wget -q -O - https://artifacts.elastic.co/downloads/kibana/kibana-${KIBANA_VERSION}-linux-x86_64.tar.gz \
 |  tar -zx \
 && mv kibana-${KIBANA_VERSION}-linux-x86_64 kibana \
 && rm -f kibana/node/bin/node kibana/node/bin/npm \
 && ln -s $(which node) kibana/node/bin/node \
 && ln -s $(which npm) kibana/node/bin/npm

RUN  wget -q -O - $MIRROR/zookeeper/zookeeper-$VERSION/zookeeper-$VERSION.tar.gz | tar -xzf - -C /home/elasticsearch \
     && mv /home/elasticsearch/zookeeper-$VERSION /home/elasticsearch/zookeeper \
     && cp /home/elasticsearch/zookeeper/conf/zoo_sample.cfg /home/elasticsearch/zookeeper/conf/zoo.cfg \
     && mkdir -p /tmp/zookeeper

VOLUME ["/home/elasticsearch/zookeeper/conf", "/tmp/zookeeper"]

#CMD sh elasticsearch/bin/elasticsearch -E http.host=0.0.0.0 & kibana/bin/kibana --host 0.0.0.0 & sh /home/elasticsearch/zookeeper-$VERSION/bin/zkServer
CMD sh elasticsearch/bin/elasticsearch -E http.host=0.0.0.0 & kibana/bin/kibana --host 0.0.0.0


EXPOSE 9200 9300 5601 2181 2888 3888
