# Projeto de Redes de Computadores: Implementação do Protocolo Go-Back-N (ProtocoloParalelo)

## Descrição do Projeto

O objetivo deste projeto era implementar as funcionalidades faltantes de um protocolo Go-Back-N, conforme o código disponibilizado no Google Classroom. Este protocolo é um método de controle de fluxo de dados para redes de computadores que utiliza um número de sequência nos pacotes, um temporizador para cada pacote enviado e retransmissão no caso de pacotes perdidos. A arquitetura desenvolvida inclui duas classes principais: `EnviaDados` e `RecebeDados`, onde uma será responsável por enviar dados em um computador e a outra por receber dados em outro computador.

## Estrutura do Projeto

- `EnviaDados`: Classe responsável por enviar os pacotes de dados.
- `RecebeDados`: Classe responsável por receber os pacotes de dados.

## Funcionalidades a Implementar

### 1. Número de Sequência nos Pacotes
Cada pacote enviado deve conter um número de sequência único que permita ao receptor identificar a ordem correta dos pacotes e detectar pacotes duplicados ou perdidos.

**Tarefas:**
- Adicionar um campo de número de sequência nos pacotes.
- Implementar a lógica para incrementar e incluir o número de sequência nos pacotes enviados.

### 2. Temporizador e Retransmissão de Pacotes Perdidos
Implementar um temporizador para cada pacote enviado. Se um pacote não for reconhecido (ACK) dentro de um tempo limite, o pacote deve ser retransmitido.

**Tarefas:**
- Implementar um temporizador para monitorar o tempo de espera por um ACK.
- Implementar a lógica de retransmissão de pacotes após o tempo limite.
- Garantir que apenas os pacotes necessários sejam retransmitidos (implementação do Go-Back-N).

## Funcionalidades Já Implementadas

### Paralelismo
A funcionalidade de paralelismo já foi implementada, permitindo que múltiplos pacotes sejam enviados e recebidos simultaneamente.

## Instruções de Uso

### Requisitos

- Java Development Kit (JDK) 8 ou superior

### Estrutura do Projeto

- `src/`: Diretório contendo os arquivos de código-fonte.
  - `envio/EnviaDados.java`: Implementação da classe de envio de dados.
  - `recepcao/RecebeDados.java`: Implementação da classe de recebimento de dados.
- `README.md`: Instruções do projeto.
- `build.xml` ou `pom.xml`: Arquivo de configuração do build (Ant ou Maven, se necessário).

### Compilação e Execução

1. Faça o download do código disponibilizado no Google Classroom.
2. Implemente as funcionalidades faltantes conforme descrito acima.
3. Compile os arquivos Java e os execute nessa ordem.

4. Execute a classe `Recebe` no computador que receberá os dados:

```bash
java -cp src/execucao/Recebe.java
```

5. Execute a classe `Envia` no computador que enviará os dados:

```bash
java -cp src/execucao/Envia.java
```

## Conclusão

Este projeto tem como objetivo aprimorar o entendimento do protocolo Go-Back-N através da implementação prática de suas principais características, como o número de sequência nos pacotes e a retransmissão de pacotes perdidos utilizando temporizadores. Ao completar estas implementações, você terá uma aplicação funcional de envio e recebimento de dados que simula um ambiente de rede real com controle de fluxo e manejo de erros.

## 🤝 Colaboradores

Agradecemos às seguintes pessoas que contribuíram para este projeto:

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/caioreius">
        <img width=100 src="https://avatars.githubusercontent.com/u/87735654?v=4" width="100px;" alt="Foto do Iuri Silva no GitHub"/><br>
        <sub>
          <b>Caio Eduardo</b>
        </sub>
      </a>
    </td>
    <td align="center">
          <a href="https://github.com/gabrielpereira3">
            <img width=100 src="https://avatars.githubusercontent.com/u/58240821?v=4" width="100px;" alt="Foto do Steve Jobs"/><br>
            <sub>
              <b>Gabriel Pereira Soares</b>
            </sub>
          </a>
     </td>
  </tr>
</table>

<!---## 📝 Licença

Esse projeto está sob licença. Veja o arquivo [LICENSE]() para mais detalhes.--->

[⬆ Voltar ao topo](#projeto-de-redes-de-computadores-implementação-do-protocolo-go-back-n-protocoloparalelo)<br>

<!---Fim README.md teste--->
