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
Retorna o `Cliente` cadastrado com o atributo `id` preenchido.

### Exemplo de requisição
```JSON
{
  "cpf": "00000000000",
  "nome": "Gabriel Alberto Moura de Sá",
  "telefone": "(00)00000-0000",
  "rendimentoMensal": 100.00,
  "endereco": {
    "rua": "Nome da Rua",
    "numero": 10,
    "cep": "00000-000"
  }
}
```

### Exemplo de resposta
```JSON
{
  "id": 1,
  "cpf": 00000000000,
  "nome": "Gabriel Alberto Moura de Sá",
  "telefone": "(00)00000-0000",
  "rendimentoMensal": 100.00,
  "endereco": {
    "rua": "Nome da Rua",
    "numero": 10,
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

Cadastra um novo empréstimo. É necessário enviar o valor inicial e o `relacionamento` do empréstimo no corpo da requisição. O `relacionamento` pode ser `Ouro`, `Prata` ou `Bronze`. Retorna o objeto `Emprestimo` cadastrado com id, valor final, data inicial e data final preenchidos.

### Exemplo de requisição
```JSON
{
  "valorInicial": 100.00,
  "relacionamento": "Ouro"
}
```
### Exemplo de Resposta
```JSON
{
  "id": 1,
  "valorInicial": 100.00,
  "valorFinal": 120.000,
  "dataInicial": "2023-04-10",
  "dataFinal": "2023-05-10",
  "relacionamento": "Ouro"
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
