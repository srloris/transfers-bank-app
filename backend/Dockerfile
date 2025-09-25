# Stage 1: build com Maven + JDK11
FROM maven:3.9.4-eclipse-temurin-11 AS builder
WORKDIR /app

# Copiar arquivos de configuração primeiro para aproveitar cache do Docker
COPY pom.xml ./
COPY mvnw* ./
COPY .mvn .mvn

# Download das dependências (melhor cache)
RUN mvn dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Build da aplicação
RUN mvn clean package -DskipTests -B

# Stage 2: runtime com JRE 11
FROM eclipse-temurin:11-jre-jammy
WORKDIR /app

# Copiar apenas o JAR gerado
COPY --from=builder /app/target/*.jar app.jar

# Criar usuário não-root para segurança
RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]