FROM openjdk:11
#VOLUME /tmp
EXPOSE 8081
COPY "./target/product-0.0.1-SNAPSHOT.jar" "product.jar"
ENTRYPOINT ["java","-jar","product.jar"]