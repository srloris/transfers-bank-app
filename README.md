# Sistema de Agendamento de Transferências

## Sobre o Projeto
Sistema completo para agendamento de transferências financeiras com cálculo automático de taxas baseado em regras específicas.

## Tecnologias Utilizadas

**Backend:**
- Java 11
- Spring Boot 2.7.x
- Spring Data JPA
- H2 Database (in-memory)
- Maven

**Frontend:**
- Vue.js 3
- Bootstrap 5
- Axios
- Vite

**Containerização:**
- Docker
- Docker Compose
- Nginx

## Decisões Arquiteturais

### Backend (Spring Boot)
- **Padrão RESTful** para APIs clean e sem estado
- **DTO Pattern** para separação entre entidades de domínio e transferência de dados
- **Service Layer** para encapsular regras de negócio (cálculo de taxas)
- **H2 Database** para persistência em memória (conforme exigência)
- **Validações** com Bean Validation para integridade dos dados

### Frontend (Vue.js 3)
- **Composition API** para melhor organização do código
- **Componentização** para reutilização e manutenibilidade
- **Bootstrap 5** para UI responsiva e profissional
- **Axios** para consumo da API com tratamento de erros

### Infraestrutura
- **Docker Compose** para orquestração de containers
- **Nginx** como reverse proxy e servidor web
- **Multi-stage builds** para otimização de imagens Docker

## Regras de Negócio Implementadas

### Cálculo de Taxas
| Dias | Taxa Fixa | Taxa Variável | Status |
|------|-----------|---------------|---------|
| 0    | R$ 3,00   | 2,5%          | ✅ |
| 1-10 | R$ 12,00  | 0,0%          | ✅ |
| 11-20| R$ 0,00   | 8,2%          | ✅ |
| 21-30| R$ 0,00   | 6,9%          | ✅ |
| 31-40| R$ 0,00   | 4,7%          | ✅ |
| 41-50| R$ 0,00   | 1,7%          | ✅ |

**Validação:** Sistema impede transferências sem taxa aplicável.

## Como Executar

### Pré-requisitos
- Docker 20.10+
- Docker Compose 2.0+
- 2GB RAM disponível

### Execução Rápida
```bash
git clone https://github.com/srloris/transfers-bank-app.git
cd transfers-bank-app
docker-compose up --build
```

### Acesso

- Frontend: http://localhost
- Backend API: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console
    - JDBC URL: jdbc:h2:mem:testdb
    - User: sa
    - Password: 