# Integração Hubspot - Teste Meetime
Microserviço desenvolvido como teste técnico

## Versões utilizadas
- Spring boot - 3.4.4
- Java 21
- Maven

## Configurações do arquivo application.yml (Alterar conforme necessidade)
- Porta de execução da aplicação:
  - server.port (Padrão - 8080)
- URL de autorização do Hubspot:
  - hubspot.auth-url (Padrão - https://app.hubspot.com/oauth/authorize)
- URL de redirecionamento do fluxo de autorização do Hubspot:
  - hubspot.redirect-url (Padrão - http://localhost:8080/auth/callback)
- URL base da API do Hubspot:
  - hubspot.api-base-url (Padrão - https://api.hubapi.com)
- URI de geração do token de acesso do Hubspot
  - hubspot.token-url (Padrão - /oauth/v1/token)
- URI da API para criação de contatos
  - hubspot.contact-uri (Padrão - /crm/v3/objects/contacts)
- URL de envio do webhook
  - hubspot.webhook.url (Padrão usado para testes - https://smee.io/WQUuXCUpdOVLpSB)
- Máximo de tempo permitido para receber um evento do webhook
  - hubspot.webhook.max-timestamp (Padrão da documentação hubspot 300000 ms - 5 minutos)

## Requisitos para execução da aplicação
- Java que suporte execução de pacotes compilados na versão 21.
- Variáveis de ambiente **obrigatórias**:
  - CLIENT_ID - Contendo o seu client ID da aplicação do Hubspot
  - CLIENT_SECRET - Contendo o seu client secret da aplicação do Hubspot
- Configuração do hubspot.webhook.url para o endereço que fará o encaminhamento dos eventos, já que será teste local. (Obrigatório apenas para o funcionamento correto do endpoint do webhook)

## Como executar a aplicação
1. Obter o pacote integracaohs.jar presente na pasta src/target deste projeto.
2. Certificar-se de que as variáveis citadas no item anterior estejam exportadas no ambiente em que será executada a aplicação:
  - [Criando variáveis de ambiente no Windows ou Linux](https://www.alura.com.br/artigos/configurar-variaveis-ambiente-windows-linux-macos?srsltid=AfmBOorgaeAvlMMlPX2XczCbFYxV4L_470tPMdYQOVxu6Ytmi0kUIOtb)
3. Certificar-se de estar utilizando uma versão do java suportada, conforme requisito descrito acima. (Compilado em Java 21)
4. Executar o arquivo integracaohs.jar.
5. Em caso de falha, execute o arquivo diretamente via CMD para verificar os possíveis erros:
    1. Abra o CMD.
    2. Navegue até o local do arquivo integracaohs.jar.
    3. Execute o comando a seguir:
    4. `java -jar integracaohs.jar`
    5. Caso necessário você pode especificar o caminho completo do java em caso de mais de uma instalação java na máquina:
    6. `"C:\CaminhoDoJDK\bin\java" -jar integracaohs.jar`

## Erros comuns
- Versão do Java não suporta execução do programa que foi compilado em uma versão mais recente
  - Solução: Fazer o download do pacote java que suporte a execução de pacotes compilados na versão 21+ e executar seguindo os passos do item anterior (Execução via CMD)
- Variáveis de ambiente não exportadas
  - Solução: Exportar as variáveis corretamente (CLIENT_ID e CLIENT_SECRET)
 
## Endpoints disponíveis
### Geração da Authorization URL
**Endpoint:** `GET /auth/url`  
- Responsável por retornar a URL de autenticação OAuth do Hubspot.
- Retornará a URL completa para uso.
### Processamento do callback da autenticação
**Endpoint:** `GET /auth/callback`  
- Responsável por processar o callback gerado na autenticação.
- Retornará seu token de acesso.
### Criação de contato
**Endpoint:** `POST /contact/create`  
**Header obrigatório:** `Authorization Bearer seucódigogeradonoendpointacima`  
**Body obrigatório:**  
```json
{
    "firstname" : "Fulano",
    "lastname" : "Exemplo",
    "email" : "email@exemplo.com"
}
```

- Responsável por criar um usuário utilizando a API da aplicação do Hubspot.
- Retornará o objeto criado ou o erro com seu código HTTP.
### Recebimento de eventos do webhook do Hubspot
**Endpoint:** `POST /webhook/events`
- Responsável por processar os eventos enviados pelo webhook configurado na aplicação do Hubspot.

## Exemplo de uso
- Considerações:
  -  Será considerado que as configurações padrões não foram alteradas estaremos executanto a aplicação localmente na porta 8080
  -  A aplicação do hubspot está configurada para permitir os scopes crm.objects.contacts.read e crm.objects.contacts.write.  
 
1. Acessar no navegador o endpoint que gerará a URL: (Ou utilizar algum utilitário, por exemplo o Postman)
   - `http://localhost:8080/auth/url`
2. Com a URL retornada podemos copiar o endereço gerado e colar novamente no navegador. (Essa etapa deve ser realizada no navegador devido à necessidade de aceite da autorização)
   - Sua URL deverá ser parecida com esta: `https://app.hubspot.com/oauth/authorize?client_id=seuclientID&scope=crm.objects.contacts.read%20crm.objects.contacts.write&redirect_uri=http://localhost:8080/auth/callback`
3. Após realizar o aceite da autorização de geração do acesso, será exibido o seu token de acesso na tela, copie-o e não o compartilhe.
   - O retorno será similar a esse: `Bearer token de acesso: cadeiadecarcteresquerepresentaseutoken`
   - Copiar apenas o conteúdo após os dois pontos, sem espaços.
4. Temos o nosso token de acesso em mãos, com ele podemos fazer a chamada para a criação de um usuário.
5. Utilizando o postman, neste exemplo, crie uma requisição com as seguintes opções: (Ou utilize o gerador de requisições da sua preferência)
   - Método: POST
   - Authorization type: Bearer Token
   - Cole seu token no espaço descrito e o postman montará o header para a requisição.
   - Na aba Body escolha a opção "raw" e no canto direito "JSON"
   - Crie o body conforme o exemplo no item "Endpoints disponíveis" logo acima neste arquivo.
6. Envie sua requisição.  

Agora se tudo ocorreu coforme o esperado seu usuário foi criado, caso contrário você receberá o retorno do erro gerado na requisição.  
Se o webhook estiver encaminhando as chamadas para o endpoint correto, você receberá uma mensagem na console informando que o evento de usuário criado foi recebido pelo webhook.

