# Sistema de Gerenciamento de Inventário e Pedidos

## Informações Acadêmicas

**Aluno:** Frank Augusto Neri de Oliveira  
**Curso:** Desenvolvimento de Software Multiplataforma  
**Instituição:** FATEC Praia Grande  
**Disciplina:** Técnicas de Programação 2  
**Data:** Outubro de 2025

---

## 1. Introdução

Este projeto consiste em um sistema de gerenciamento de inventário e pedidos desenvolvido em Java, utilizando console como interface de usuário. O sistema permite o controle completo de produtos em estoque e a criação de pedidos de clientes, com persistência de dados em arquivos JSON.

### 1.1 Objetivo

Desenvolver um sistema robusto e flexível que demonstre a aplicação prática de conceitos avançados de programação Java, incluindo:
- Manipulação de JSON com Jackson Databind
- Validações com expressões regulares (Regex)
- Uso de Enums para tipos seguros
- Programação funcional com Optional
- Arquitetura em camadas

---

## 2. Tecnologias Utilizadas

### 2.1 Linguagem e Versão
- **Java:** 11 ou superior
- **Build Tool:** Maven

### 2.2 Bibliotecas e Dependências

| Biblioteca | Versão | Finalidade |
|------------|--------|------------|
| Jackson Databind | 2.15.2 | Serialização e desserialização JSON |
| Jackson Datatype JSR310 | 2.15.2 | Suporte para LocalDateTime |

---

## 3. Arquitetura do Sistema

O projeto foi desenvolvido utilizando uma **arquitetura em camadas**, promovendo separação de responsabilidades e facilitando manutenção e testes.

### 3.1 Estrutura de Pacotes

```
com.gerenciamentoInventario.gerenciamentoInventario/
├── Model/              (Entidades de domínio)
│   ├── Produto.java
│   ├── Pedido.java
│   ├── ItemPedido.java
│   └── StatusPedido.java
├── Repository/         (Camada de persistência)
│   ├── ProdutoRepository.java
│   └── PedidoRepository.java
├── Service/            (Lógica de negócio)
│   ├── ProdutoService.java
│   └── PedidoService.java
├── Util/               (Utilitários)
│   ├── JsonUtil.java
│   └── ValidadorUtil.java
└── SistemaInventario.java (Classe principal)
```

### 3.2 Responsabilidades das Camadas

#### Model (Modelo)
Representa as entidades do domínio da aplicação. Contém apenas atributos, getters, setters e métodos relacionados aos dados.

#### Repository (Repositório)
Responsável exclusivamente pela persistência de dados. Realiza operações de leitura e escrita em arquivos JSON, abstraindo os detalhes de armazenamento.

#### Service (Serviço)
Contém toda a lógica de negócio da aplicação. Valida dados, aplica regras de negócio e coordena operações entre diferentes entidades.

#### Util (Utilitários)
Fornece funções auxiliares reutilizáveis, como validações e manipulação de JSON.

#### SistemaInventario
Classe principal que gerencia a interface com o usuário através do console, exibindo menus e capturando entradas.

---

## 4. Funcionalidades Implementadas

### 4.1 Gerenciamento de Produtos

#### Adicionar/Atualizar Produto
- Permite cadastrar novos produtos com ID único, nome, preço e quantidade
- Se o ID já existir, atualiza a quantidade em estoque (soma com a quantidade existente)
- Validações: ID não vazio, nome com mínimo 3 caracteres, preço positivo, quantidade não negativa

#### Listar Produtos
- Exibe todos os produtos cadastrados com suas informações completas
- Mostra o total de produtos no sistema

#### Buscar Produto por ID
- Permite localizar um produto específico pelo seu identificador
- Utiliza Optional para tratamento seguro de ausência de dados

### 4.2 Gerenciamento de Pedidos

#### Criar Pedido
- Solicita nome e e-mail do cliente (com validação de formato)
- Permite adicionar múltiplos itens ao pedido
- Verifica disponibilidade de estoque antes de adicionar cada item
- Calcula automaticamente o valor total do pedido
- Reduz o estoque dos produtos ao finalizar o pedido

#### Listar Pedidos
- Exibe resumo de todos os pedidos cadastrados
- Mostra ID, cliente, status e valor total

#### Ver Detalhes do Pedido
- Apresenta informações completas de um pedido específico
- Lista todos os itens com quantidades, preços unitários e subtotais
- Exibe o valor total do pedido

#### Atualizar Status do Pedido
- Permite alterar o status do pedido dinamicamente
- Apresenta todas as opções de status disponíveis do enum
- Estados disponíveis: NOVO, PROCESSANDO, ENVIADO, ENTREGUE, CANCELADO

---

## 5. Conceitos Técnicos Aplicados

### 5.1 Jackson Databind (ObjectMapper e JsonNode)

#### ObjectMapper
Utilizado para serialização e desserialização automática de objetos Java para JSON e vice-versa.

**Configurações aplicadas:**
```java
mapper.enable(SerializationFeature.INDENT_OUTPUT);           // JSON formatado
mapper.registerModule(new JavaTimeModule());                 // Suporte a LocalDateTime
mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Datas em formato ISO
```

#### JsonNode
Empregado no carregamento de produtos para demonstrar navegação flexível em estruturas JSON. Permite iterar sobre elementos JSON antes de convertê-los em objetos Java.

**Exemplo de uso:**
```java
JsonNode rootNode = JsonUtil.carregarComoJsonNode(ARQUIVO_PRODUTOS);
for (JsonNode node : rootNode) {
    Produto produto = JsonUtil.converterJsonNode(node, Produto.class);
}
```

### 5.2 Java Regex (Pattern e Matcher)

Implementado para validação rigorosa do formato de e-mail dos clientes.

**Pattern utilizado:**
```java
private static final Pattern EMAIL_PATTERN = Pattern.compile(
    "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
);
```

**Vantagens:**
- Pattern compilado uma única vez (performance)
- Validação precisa do formato de e-mail
- Tratamento de casos nulos e strings vazias

### 5.3 Enums (Java)

O enum `StatusPedido` define os estados possíveis de um pedido de forma segura e tipada.

**Estrutura:**
```java
public enum StatusPedido {
    NOVO("Novo - Pedido recém criado"),
    PROCESSANDO("Processando - Em preparação"),
    ENVIADO("Enviado - A caminho do cliente"),
    ENTREGUE("Entregue - Recebido pelo cliente"),
    CANCELADO("Cancelado - Pedido cancelado");
    
    private final String descricao;
}
```

**Benefícios:**
- Type-safety (segurança de tipos)
- Código mais legível e manutenível
- Geração dinâmica de menus a partir dos valores do enum
- Impossível usar valores inválidos

### 5.4 Optional (Java 8+)

Utilizado extensivamente para evitar `NullPointerException` e tornar o código mais expressivo.

**Métodos utilizados:**

#### buscarProdutoPorId / buscarPedidoPorId
```java
public Optional<Produto> buscarProdutoPorId(String id) {
    return Optional.ofNullable(produtoRepository.buscarPorId(id));
}
```

#### ifPresent
```java
pedidoOpt.ifPresent(pedido -> {
    pedido.setStatus(novoStatus);
    pedidoRepository.salvar(pedido);
});
```

#### ifPresentOrElse
```java
produtoService.buscarProdutoPorId(id).ifPresentOrElse(
    produto -> System.out.println(produto),
    () -> System.out.println("Produto não encontrado")
);
```

#### map e orElse
```java
public boolean verificarEstoque(String id, int quantidade) {
    Optional<Produto> produto = buscarProdutoPorId(id);
    return produto.map(p -> p.getQuantidadeEstoque() >= quantidade).orElse(false);
}
```

---

## 6. Persistência de Dados

### 6.1 Formato de Armazenamento

Os dados são persistidos em arquivos JSON na raiz do projeto:
- `produtos.json`: Armazena o inventário de produtos
- `pedidos.json`: Armazena os pedidos realizados

### 6.2 Estrutura dos Arquivos JSON

#### produtos.json
```json
[
  {
    "id": "PROD001",
    "nome": "Notebook Dell Inspiron",
    "preco": 3500.00,
    "quantidadeEstoque": 15
  }
]
```

#### pedidos.json
```json
[
  {
    "id": "PED001",
    "nomeCliente": "João Silva",
    "emailCliente": "joao@email.com",
    "itens": [
      {
        "produtoId": "PROD001",
        "nomeProduto": "Notebook Dell Inspiron",
        "precoUnitario": 3500.00,
        "quantidade": 2
      }
    ],
    "status": "NOVO",
    "dataCriacao": "2024-10-27T14:30:00"
  }
]
```

### 6.3 Carregamento e Salvamento

- **Carregamento automático:** Os dados são carregados automaticamente ao iniciar o sistema
- **Salvamento automático:** Cada operação que modifica dados (adicionar produto, criar pedido, etc.) salva imediatamente no arquivo
- **Criação automática:** Se os arquivos não existirem, são criados automaticamente com arrays vazios

---

## 7. Validações Implementadas

### 7.1 Validação de Produtos

| Campo | Regra de Validação |
|-------|-------------------|
| ID | Não pode ser nulo ou vazio |
| Nome | Mínimo de 3 caracteres |
| Preço | Deve ser maior que zero |
| Quantidade | Deve ser maior ou igual a zero |

### 7.2 Validação de Pedidos

| Campo | Regra de Validação |
|-------|-------------------|
| Nome do Cliente | Mínimo de 3 caracteres |
| E-mail | Formato válido (pattern regex) |
| Quantidade de Item | Deve ser maior que zero |
| Estoque | Verifica disponibilidade antes de adicionar |

---

## 8. Fluxo de Execução

### 8.1 Fluxo de Criação de Pedido

```
1. Usuário escolhe "Criar Novo Pedido"
2. Sistema solicita nome do cliente
3. Sistema solicita e-mail (valida com regex)
4. Sistema gera ID único para o pedido
5. Sistema cria objeto Pedido (status = NOVO)
6. Loop de adição de itens:
   a. Sistema lista produtos disponíveis
   b. Usuário seleciona produto por ID
   c. Usuário informa quantidade
   d. Sistema verifica estoque usando Optional
   e. Sistema adiciona item ao pedido
   f. Sistema pergunta se deseja adicionar mais itens
7. Sistema finaliza pedido:
   a. Reduz estoque de cada produto
   b. Salva pedido em pedidos.json
   c. Salva produtos atualizados em produtos.json
8. Sistema exibe detalhes completos do pedido criado
```

### 8.2 Fluxo de Atualização de Status

```
1. Usuário escolhe "Atualizar Status do Pedido"
2. Sistema solicita ID do pedido
3. Sistema busca pedido usando Optional
4. Se pedido existe:
   a. Exibe status atual
   b. Lista todos os status disponíveis do enum dinamicamente
   c. Usuário escolhe novo status
   d. Sistema atualiza e salva
5. Se pedido não existe:
   a. Exibe mensagem de erro
```

---

## 9. Tratamento de Erros

### 9.1 Erros de Entrada do Usuário

- **Valores inválidos:** Capturados com try-catch e valores padrão são usados (0 ou 0.0)
- **Opções de menu inválidas:** Exibe mensagem de erro e solicita nova entrada
- **IDs não encontrados:** Tratados com Optional, exibindo mensagens apropriadas

### 9.2 Erros de Persistência

- **Arquivo não encontrado:** Sistema cria automaticamente arquivo vazio
- **JSON inválido:** Mensagem de erro é exibida, mas sistema continua executando
- **Erro ao salvar:** Mensagem de erro específica é mostrada ao usuário

---

## 10. Instruções de Compilação e Execução

### 10.1 Pré-requisitos

- JDK 11 ou superior instalado
- Maven instalado e configurado
- IDE (IntelliJ IDEA, Eclipse ou VS Code com extensões Java)

### 10.2 Compilação via Maven

```bash
mvn clean compile
```

### 10.3 Execução via Maven

```bash
mvn exec:java -Dexec.mainClass="com.gerenciamentoInventario.gerenciamentoInventario.SistemaInventario"
```

### 10.4 Geração de JAR executável

```bash
mvn clean package
java -jar target/gerenciamentoInventario-1.0-SNAPSHOT.jar
```

### 10.5 Execução via IDE

1. Abra o projeto na IDE
2. Localize a classe `SistemaInventario.java`
3. Execute o método `main()`

---

## 11. Exemplos de Uso

### 11.1 Cadastrar Produto

```
Escolha: 1 (Gerenciar Produtos)
Escolha: 1 (Adicionar/Atualizar Produto)
ID do Produto: PROD001
Nome: Notebook Dell
Preço: R$ 3500.00
Quantidade em Estoque: 10
```

### 11.2 Criar Pedido

```
Escolha: 2 (Gerenciar Pedidos)
Escolha: 1 (Criar Novo Pedido)
Nome do Cliente: João Silva
E-mail do Cliente: joao@email.com
ID do Produto: PROD001
Quantidade: 2
Deseja adicionar mais itens? N
```

### 11.3 Atualizar Status

```
Escolha: 2 (Gerenciar Pedidos)
Escolha: 4 (Atualizar Status do Pedido)
Digite o ID do pedido: PED001
Escolha o novo status: 2 (Processando)
```

---

## 12. Pontos de Destaque do Projeto

### 12.1 Boas Práticas Aplicadas

- **Separação de responsabilidades:** Cada classe tem uma única responsabilidade bem definida
- **Encapsulamento:** Atributos privados com acesso controlado via getters/setters
- **Imutabilidade:** Pattern compilado uma única vez, atributos final onde apropriado
- **Programação defensiva:** Validações em múltiplas camadas
- **Código limpo:** Métodos pequenos e focados, nomes descritivos

### 12.2 Conceitos Avançados

- **Method References:** Uso de `ItemPedido::getSubtotal` em streams
- **Lambda Expressions:** Utilizadas em Optional e forEach
- **Streams API:** Para cálculos e operações em coleções
- **Generics:** Métodos genéricos em JsonUtil e retornos Optional

### 12.3 Demonstração dos Requisitos

| Requisito | Implementação |
|-----------|---------------|
| Jackson ObjectMapper | Configuração completa em JsonUtil |
| Jackson JsonNode | Carregamento de produtos com navegação flexível |
| Java Regex | Validação de e-mail com Pattern e Matcher |
| Enums | StatusPedido com descrições e uso dinâmico |
| Optional | Múltiplos métodos retornando Optional, uso de ifPresent, ifPresentOrElse, map, orElse |

---

## 13. Possíveis Melhorias Futuras

### 13.1 Funcionalidades

- Cancelamento de pedidos com reposição de estoque
- Histórico de alterações de status
- Relatórios de vendas
- Sistema de busca avançada (por nome, faixa de preço, etc.)
- Gerenciamento de categorias de produtos

### 13.2 Técnicas

- Implementação de testes unitários (JUnit)
- Logging com SLF4J
- Interface gráfica (JavaFX ou Swing)
- Migração para banco de dados relacional
- API REST para acesso externo
- Sistema de autenticação de usuários

---

## 14. Conclusão

Este projeto demonstra a aplicação prática de conceitos fundamentais e avançados de programação Java, incluindo manipulação de JSON, validações com expressões regulares, uso de tipos seguros com enums e programação funcional com Optional. 

A arquitetura em camadas proporciona um código organizado, manutenível e extensível, enquanto a persistência em JSON permite um armazenamento simples e legível dos dados.

O sistema atende completamente aos requisitos propostos e representa uma base sólida que pode ser expandida com novas funcionalidades conforme necessário.

---

## 15. Referências

- Oracle Java Documentation: https://docs.oracle.com/en/java/
- Jackson Documentation: https://github.com/FasterXML/jackson-docs
- Java Regex Tutorial: https://docs.oracle.com/javase/tutorial/essential/regex/
- Java Optional Guide: https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html
- Maven Documentation: https://maven.apache.org/guides/

---

**Desenvolvido por:** Frank Augusto Neri de Oliveira  
**FATEC Praia Grande - DSM**  
**Técnicas de Programação 2 - 2024**
