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

Use o arquivo __initialScript.sql__ que está na pasta infra/Scripts para criar o usuário e a base de dados.

Após criado a base de dados, restaure o backup que está na pasta infra:

```
docker exec -i t2tierp-postgresql psq -U t2tierp < script_bd_infra_controle_acesso.backup
```

Após restaurar o backup, altere a senha do usuário admin para '123456':

```
docker exec -i t2tierp-postgresql psql -U t2tierp -d t2tierp -c "update usuario set senha = 'e10adc3949ba59abbe56e057f20f883e' where id = 1"
```