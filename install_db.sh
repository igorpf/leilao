#!/bin/bash
echo "Deletando a base atual"
sudo -u postgres dropdb leilao
sudo -u postgres dropdb leilao_test
echo "Criando usuário"
sudo -u postgres psql -c "CREATE USER leilao WITH PASSWORD 'leiloeiro'"
echo "Criando nova base de dados"
sudo -u postgres createdb leilao -O leilao
sudo -u postgres createdb leilao_test -O leilao
sudo -u postgres psql leilao_test < src/test/resources/createPG.sql
sudo -u postgres psql leilao_test < src/test/resources/pgTest.sql
