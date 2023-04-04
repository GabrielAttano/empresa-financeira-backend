# Empresa Financeira - Backend

## Sobre o projeto
Neste projeto foi desenvolvida uma RESTful API responsável pelo sistema de empréstimos de uma empresa financeira. Este projeto é o teste final do curso de capacitação Java e Angular oferecido pela [Minsait](https://www.minsait.com/pt).

## Rotas
A API possui as seguintes rotas:

## Índice
* [Cadastro de cliente](#Cadastro-de-Cliente)
* [Recuperação de clientes](#Recuperação-de-Clientes)
* [Recuperação de cliente](#Recuperação-de-Cliente)
* [Deletar cliente](#Deletar-Cliente)

## Cadastro de Cliente
`POST /api/v1/empresa-financeira/clientes`

Cadastra um novo cliente na base de dados. É necessário enviar um objeto Cliente no corpo da requisição.
Retorna o objeto `Cliente` cadastrado com o atributo `id` preenchido.

### Exemplo de requisição
```JSON
{
  "cpf": "XXXXXXXXXXX",
  "nome": "Gabriel Alberto Moura de Sá",
  "telefone": "(XX)XXXXX-XXXX",
  "rendaMensal": 0,
  "endereco": {
    "rua": "São José",
    "numero": 104,
    "cep": "XXXXX-XXX"
  }
}
```
onde cada X é um dígito, e todos os campos devem estar devidamente preenchidos.

### Exemplo de resposta
```JSON
{
  "id": 1,
  "nome": "Gabriel Alberto Moura de Sá",
  "telefone": "(00)00000-0000",
  "rendaMensal": 0,
  "endereco": {
    "rua": "São José",
    "numero": 104,
    "cep": "00000-000"
  }
}
```

## Recuperação de Clientes
`GET /api/v1/empresa-financeira/clientes`

Retorna uma lista contendo todos os objetos `Cliente` registrados no banco de dados.

## Recuperação de Cliente
`GET /api/v1/empresa-financeira/clientes/{cpf}`

Retorna um objeto `Cliente` registrado no banco de dados com o CPF informado.

## Deletar Cliente
`DELETE /api/v1/empresa-financeira/clientes/{cpf}`

Deleta um `Cliente` registrado no banco de dados com o CPF informado.
