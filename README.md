# Api de tabela tarifária de Água

## Pré Requisitos

- Java 21
- PostgreSQL 17
- Docker (Opcional)

---

## Baixando o Projeto
- Clone esse repositório e entre na pasta:
```bash
git clone https://github.com/j0n4t45d3v/api-tabela-tarifaria.git
cd api-tabela-tarifaria/
``` 
## Executando o Projeto

### Executando Usando Docker:
Execute o comando abaixo:
```bash
docker compose up -d
``` 

### Executando Sem Docker: 
1. Faça o build do projeto:
```bash
./mvnw clean package -DskipTests
``` 
2. Garanta que tenha um banco rodando e execute o seguinte comando: 
```bash
java -jar target/api-tabela-tarifaria-0.0.1-SNAPSHOT.jar
``` 

---

## Configurão do banco de dados
Para o projeto funcionar corretamente é necessário informar essas variaveis de ambiente.
> Obs.: Caso tenha subido a API usando docker essa parte da configuração do banco de dados pode ser ignorada pois o arquivo de configuração do docker já configura um banco e define as variaveis para o projeto rodar corretamente. 
```bash
export SPRING_DB_JDBC_URL=jdbc:postgresql://<host>:<port>/<nome_do_banco>
export SPRING_DB_USERNAME=<usuario>
export SPRING_DB_PASSWORD=<senha>
```

> Caso não informado essas variaveis de ambiente a API por padrão irá usar as seguintes credenciais 
```env
SPRING_DB_JDBC_URL = jdbc:postgresql://localhost:5432/db_tabela_tarifaria
SPRING_DB_USERNAME = postgres
SPRING_DB_PASSWORD = senhasecreta
```
---

## Exemplos Requisições de Resposta

### Exemplo de Response de error:
> Obs.: Nesse projeto as mensagens de erros são pradronizadas então todas seguem o modelo abaixo:
```text
HTTP/1.1 422 Unprocessable Entity

{
  "codigo": "FALHA_NA_VALIDACAO",
  "erro": "Requisição enviada está inválida",
  "detalhes": {
    "tarifas[].categoria": [
      "Informe a 'tarifas[].categoria' do consumidor dessa tarifa."
    ]
  }
}
```

### 1. Criação de uma tabela tarifaria completa com categoria + faixas + valores;
#### Request
```curl
curl -X 'POST' \
  'http://localhost:8080/api/tabelas-tarifarias' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "nome": "TARIFA - 2026",
  "vigente": {
    "de": "2026-01-01",
    "ate": "2026-12-31"
  },
  "tarifas": [
    {
      "categoria": "COMERCIAL",
      "faixas": [
        {
          "de": 0,
          "ate": 10,
          "valorUnitario": 1
        },
        {
          "de": 11,
          "ate": 20,
          "valorUnitario": 2
        },
        {
          "de": 21,
          "ate": 99999,
          "valorUnitario": 3
        }
      ]
    },
    {
      "categoria": "INDUSTRIAL",
      "faixas": [
        {
          "de": 0,
          "ate": 10,
          "valorUnitario": 2.50
        },
        {
          "de": 11,
          "ate": 99999,
          "valorUnitario": 4.50
        }
      ]
    }
  ]
}'
```

#### Response
```text
HTTP/1.1 201 Created
Location: /tabelas-tarifarias/{id}
```
### 2. Listar Tabelas Tarifarias de Água
#### Request
```curl
curl -X 'GET' \
  'http://localhost:8080/api/tabelas-tarifarias' \
  -H 'accept: */*'
```
#### Response
```text
HTTP/1.1 200 OK
Content-Type: application/json 

[
  {
    "id": 1,
    "nome": "TARIFA - 2026",
    "dataVigenciaInicial": "2026-01-01",
    "dataVigenciaFinal": "2026-12-31",
    "faixasConsumo": [
      {
        "ate": 20,
        "categoriaConsumidor": {
          "id": 2,
          "nome": "INDUSTRIAL"
        },
        "de": 11,
        "id": 7,
        "valorUnitario": 4.5
      },
      {
        "ate": 10,
        "categoriaConsumidor": {
          "id": 2,
          "nome": "INDUSTRIAL"
        },
        "de": 0,
        "id": 8,
        "valorUnitario": 2.5
      },
      {
        "ate": 10,
        "categoriaConsumidor": {
          "id": 1,
          "nome": "COMERCIAL"
        },
        "de": 0,
        "id": 5,
        "valorUnitario": 1
      },
      {
        "ate": 20,
        "categoriaConsumidor": {
          "id": 1,
          "nome": "COMERCIAL"
        },
        "de": 11,
        "id": 6,
        "valorUnitario": 2
      }
    ]
  }
]
```

### 3. Deleção de uma tabela tarifaria pelo id.
#### Request
```curl
curl -X 'DELETE' \
  'http://localhost:8080/api/tabelas-tarifarias/{id}' \
  -H 'accept: */*'
```
#### Response Sucesso
```text
HTTP/1.1 204 No Content
```

### 4. Calcular valor a pagar sobre o consumo de água.
#### Request
```curl
curl -X 'POST' \
  'http://localhost:8080/api/calculos' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "categoria": "INDUSTRIAL",
  "consumo": 18
}'
```
#### Response
```text
HTTP/1.1 200 OK
Content-Type: application/json 

{
  "categoria": "INDUSTRIAL",
  "consumoTotal": 18,
  "valorTotal": 61,
  "detalhamento": [
    {
      "faixa": {
        "inicio": 0,
        "fim": 10
      },
      "m3Cobrados": 10,
      "valorUnitario": 2.5,
      "subtotal": 25
    },
    {
      "faixa": {
        "inicio": 11,
        "fim": 20
      },
      "m3Cobrados": 8,
      "valorUnitario": 4.5,
      "subtotal": 36
    }
  ]
}
```

---

## Como testar a API
Para testar a API primeiramente siga os passos em [Executando o projeto](#executando-o-projeto) e após ter conseguido rodar o projeto acesse [http://localhost:8080/api/swagger-ui/index.html](http://localhost:8080/api/swagger-ui/index.html) para fazer as requisições. 
> Obs.: Nesse projeto também tem testes unitário, você pode executar eles usando o comando `./mvnw test` 