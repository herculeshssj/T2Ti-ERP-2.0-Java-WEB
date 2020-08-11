Projeto T2Ti ERP 2.0
====================

![](http://t2ti.com/images/erp/logo_erp_2.png)

Aqui é possível baixar o código fonte do Projeto T2Ti ERP 2.0 em Java WEB. Este treinamento formou milhares de desenvolvedores no Brasil e ajudou centenas deles a lançar seu próprio ERP no mercado. Mais informações sobre o treinamento podem ser encontradas no seguinte link: http://t2ti.com/erp2/.


## T2Ti ERP 2.0 - Escopo

![](http://t2ti.com/images/erp/t2ti_erp_2.jpg)

### Bloco Administrativo

**O Bloco Administrativo é formado por cinco módulos:**

- **Cadastros Base:** engloba os cadastros que são usados por diversos módulos do ERP.
- **[GED - Gestão Eletrônica de Documentos](http://t2ti.com/erp2/artigos/GED.pdf)**
- **[Gerador de Etiquetas](http://t2ti.com/erp2/artigos/GeradorEtiquetas.pdf)**
- **[Agenda Corporativa](http://t2ti.com/erp2/artigos/AgendaCorporativa.pdf)**
- **[BI - Business Intelligence](http://t2ti.com/erp2/artigos/BI.pdf)**

## Forma de Desenvolvimento Java Web

![](http://t2ti.com/images/erp/jsf-arch.jpg)

O ERP Java Web é feito com JSF como tecnologia base e frameworks auxiliares que são apresentados aos Participantes durante o treinamento. Como uma aplicação Web, tudo o que o usuário vai precisar é de um browser para usar o ERP.

## Ambiente de desenvolvimento

- Java 11
- Eclipse Java EE
- JBoss Tools
- Wildfly 11
- PostgreSQL 11
- Docker

## Configuração do ambiente

### PostgreSQL

```
docker volume create t2tierp-psql-data
docker run --restart=unless-stopped --name t2tierp-postgresql -e POSTGRES_PASSWORD=postgres -p 5432:5432 -v t2tierp-psql-data:/var/lib/postgresql/data -d postgres:11
```

Use o arquivo __initialScript.sql__ que está na pasta infra/t2tierp/Scripts para criar o usuário e a base de dados.

Após criado a base de dados, restaure o backup que está na pasta infra:

```
docker exec -i t2tierp-postgresql psql -U t2tierp < script_bd_infra_controle_acesso.backup
```

Após restaurar o backup, altere a senha do usuário admin ('1') para '123456':

```
docker exec -i t2tierp-postgresql psql -U t2tierp -d t2tierp -c "update usuario set senha = 'eeafb716f93fa090d7716749a6eefa72' where id = 1"
```

Logo em seguida, execute a atualização da estrutura da base usando o Flyway configurado no projeto infra:

```
docker run --rm -it --name t2tierp-flyway --net=host -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven maven:3-openjdk-11 mvn flyway:baseline
```

```
docker run --rm -it --name t2tierp-flyway --net=host -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven maven:3-openjdk-11 mvn flyway:migrate
```

**Obs:** faça os ajustes necessários no pom.xml referente a conexão com a base.

### Eclipse

Defina o encoding do workspace para ISO-8859-1.

Instale o JBoss Tools no Eclipse pelo Eclipse Marketplace. Deixe marcado somente a opção "JBoss AS, WildFly & EAP Server Tools" na janela de seleção de componentes.

Após a instalação do JBoss Tools, defina o Wildfly 11 como o servidor principal na view Server, deixando as opções padrão marcadas.

Importe os projetos que serão trabalhados pela perspectiva Git.

Não esqueça de realizar um build no projeto através do Maven -> Update projects e de realizar o mvn install através do Run As -> Maven install.

Para rodar qualquer um dos projetos, clique direito -> Run As -> Run On Server.

### JBoss

Inicie o Wildfly 11. Entre na pasta bin do Wildfly 11 e execute o script __datasource.cli__. O script está na pasta infra.

```
./jboss-cli.sh --file=datasource.cli 
```

## Realizando o deploy

Cada projeto produz seu próprio arquivo .war, que pode ser adicionado na mesma instância ou em outras instâncias de Wildfly.

Para gerar o arquivo .war, clique direito no projeto -> Run As -> Maven Install...

O arquivo .war estará na pasta target/.

Crie uma pasta chamada deployments no diretório home.

Pare qualquer instância do Wildfly que está atualmente em execução.

Suba o container do Wildfly 11:

```
docker run --name t2tierp-wildfly --net=host -v "$(pwd)"/deployments:/opt/jboss/wildfly/standalone/deployments -d jboss/wildfly:11.0.0.Final
```

Copie o arquivo datasource.cli e o driver JDBC para dentro do container:

```
docker cp datasource.cli t2tierp-wildfly:/opt/jboss
docker cp postgresql-42.2.14.jar t2tierp-wildfly:/opt/jboss
```

Entre dentro do container:

```
docker exec -it t2tierp-wildfly bash
```

Execute o script:

```
./wildfly/bin/jboss-cli.sh --file=datasource.cli
```

Saia do container e copie o arquivo WAR para a pasta deployments e aguarde finalizar o deploy.