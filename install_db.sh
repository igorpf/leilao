#!/bin/bash
echo "Deletando a base atual"
sudo -u postgres dropdb leilao
echo "Criando usuário"
sudo -u postgres psql -c "CREATE USER leilao WITH PASSWORD 'leiloeiro'"
echo "Criando nova base de dados"
sudo -u postgres createdb leilao -O leilao
