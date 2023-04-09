# Empresa Financeira - Backend

## Sobre o projeto
Neste projeto foi desenvolvida uma RESTful API responsável pelo sistema de empréstimos de uma empresa financeira. Este projeto é o teste final do curso de capacitação Java e Angular oferecido pela [Minsait](https://www.minsait.com/pt).

# Rotas
A API possui rotas para o [cliente](#Rotas-para-o-cliente) e para os [empréstimos](#Rotas-para-os-empréstimos)

# Rotas para o cliente
* [Cadastro de cliente](#Cadastro-de-Cliente)
* [Recuperação de clientes](#Recuperação-de-Clientes)
* [Recuperação de cliente](#Recuperação-de-Cliente)
* [Deletar cliente](#Deletar-Cliente)
* [Alterar cliente](#Alterar-Cliente)

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
  "relacionamento": "Prata",
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
  "relacionamento": "Prata",
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

Retorna uma lista contendo todos os `Cliente` registrados no banco de dados.

## Recuperação de Cliente
`GET /api/v1/empresa-financeira/clientes/{cpf}`

Retorna um `Cliente` registrado no banco de dados com o CPF informado.

## Deletar Cliente
`DELETE /api/v1/empresa-financeira/clientes/{cpf}`

Deleta um `Cliente` registrado no banco de dados com o CPF informado.

## Alterar Cliente
`PUT /api/v1/empresa-financeira/clientes/{cpf}`

Altera um `Cliente` registrado no banco de dados com o CPF informado. Deve ser informado o(s) atributo(s) do `Cliente` que vai ser alterado e seu valor novo. Não é possível alterar o CPF.

# Rotas para os empréstimos
* [Cadastrar Empréstimo](#cadastrar-empréstimo)
* [Recuperar Empréstimo](#recuperar-empréstimo)
* [Recuperar Empréstimos](#recuperar-empréstimos)
* [Deletar Empréstimo](#deletar-empréstimo)

## Cadastrar Empréstimo
`POST /api/v1/empresa-financeira/clientes/{cpf}/emprestimos`

Cadastra um novo empréstimo. É necessário enviar o valor inicial do empréstimo no corpo da requisição. Retorna o objeto `Emprestimo` cadastrado com o id preenchido.

### Exemplo de requisição
```JSON
{
  "valorInicial": 1000.00
}
```
### Exemplo de Resposta
```JSON
{
  "id": 1,
  "valorInicial": 5000.00,
  "valorFinal": 7000.70,
  "dataInicial": "YYYY-MM-DD",
  "dataFinal": "YYYY-MM-DD"
}
```

## Recuperar Empréstimo
`GET /api/v1/empresa-financeira/clientes/{cpf}/emprestimos/{id}`

Retorna um `Emprestimo` registrado no banco de dados com o id informado, se o `Emprestimo` pertence ao `Cliente` com o cpf informado.

## Recuperar Empréstimos
`GET /api/v1/empresa-financeira/clientes/{cpf}/emprestimos`

Retorna uma lista de `Emprestimo` do `Cliente` com o cpf informado.

## Deletar Empréstimo
`DELETE /api/v1/empresa-financeira/clientes/{cpf}/emprestimos/{id}`

Deleta um `Emprestimo` registrado no banco de dados com o id informado, se o `Emprestimo` pertence ao `Cliente` com o cpf informado.