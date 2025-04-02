# Documentação do processo

## Dependências utilizadas
- spring-boot-starter-web
	- Dependência básica
- spring-boot-starter-webflux
	- Para utilização do webclient em chamadas http.
- lombok
	- Utilizo sempre nos meus projetos para o evitar código boilerplate

## Decisões tomadas

### Âmbito geral
- Para chamadas HTTP eu normalmente utilizava RestTemplate, mas vi que está marcado como obsoleto nas novas versões do Spring. Optei por pesquisar e utilizar o Webclient em tempo de criação da solução.

### Endpoints de autenticação
- Em realação ao controller responsável pela autenticação optei por retornos em texto simples para uma melhor visualização/execução dos testes, já que as especificações pediam os enpoints com os retornos. Pensando também em facilitar a leitura em caso de algum erro, para que ela já apareça no próprio browser quando acessado o link para aceite da conexão.
- Optei por deixar as variáveis sensíveis de CLIENT_ID e CLIENT_SECRET em variáveis de ambiente, por questão de boa prática, e as demais configurações gerais de URLs no application.yml.
- Fiz um check de erro geral para as variáveis vindas do application.yml para simplificação e melhor legibilidade do código, poderia ser melhorado para um check mais robusto identificando qual variável individualmente está com problema, para um melhor retorno de erro.
- Fico ciente que a exposição dos tokens e manipulação dos mesmos ferem as práticas de segurança, mas entendo que a natureza do teste proposto deixa essa exibição/manipulação necessária, visto que não temos o front-end para lidar com as requisições de forma transparente.

### Criação de contato
- Aqui optei por fazer a chamada à API do Hubspot, capturar o erro HTTP e reencaminhar com o body no retorno na minha própria API, para seguir os códigos de erros padrões do REST. Também motivado por questões de simplificação do código.
- Os DTOs foram criados somente com os campos pertinentes à atividade, para evitar sobrecarregar com código desnecessário.
- Optei por enviar o token a ser utilizado para acesso à API do Hubspot via header Authorization padrão, já que essa aplicação de testes não tem requisito de configuração de segurança nos endpoints, então não teria conflito. Foi a forma mais simples que pude pensar, já que não havia especificação de como armazenar/consumir o token gerado. (Não temos o front-end para lidar com isso via envio de cookies ou algo similar)

### Endpoint de recebimento do Webhook
- Seguindo a documentação do Hubspot criei o check do timestamp da requisição bem como a validação do header X-HubSpot-Signature-V3.
- Deixei um simples print na console para sinalizar o recebimento do evento visto que não há detalhes do que realizar com o evento, novamente visando a simplificação do código para avaliação.

## Possíveis melhorias
- Implementação de segurança dessa API, exigindo tokens para acesso aos endpoints.
	- Isso faria com que a atual forma de envio do token para uso na API do hubspot ficasse conflitante, então teria que ser modificada também. (Enviando via outro header ou cookies via front-end)
- Abstração das classes de request e response do Hubspot
	- Por simplificação foi criado um DTO com as propriedades de contato fixas devido a natureza do teste proposto.
	- Porém, como o Hubspot utiliza essa estrutura para os outros objetos, isso poderia ser abstraído e as classes DTO de request e reponse serviriam para outros objetos também.
- Conforme já mencionado, uma criação de um check mais robusto das variáveis/propriedades do application.yml, identificando individualmente os erros.
- Por fim, uma implementação de um front-end para que toda a exposição de tokens (inerente a atividade proposta) fique transparente ao usuário e assim as boas práticas de segurança sejam seguidas devidamente.
