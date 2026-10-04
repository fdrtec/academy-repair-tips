# OWASP ZAP via MCP

Esta integração conecta o Copilot Chat ao servidor MCP oficial do add-on MCP
Integration do OWASP ZAP. Ela fica restrita ao ZAP local e não expõe a chave no
repositório.

## Instalação

1. O OWASP ZAP `2.17.0` foi instalado localmente em `~/.local/opt/zap`.
   Para abrir a interface, execute `~/.local/opt/zap/zap.sh`.
   Alternativamente, instale pelo site oficial: <https://www.zaproxy.org/download/>.
2. Abra o ZAP e acesse `Tools > Manage Add-ons`.
3. Na aba `Marketplace`, instale o add-on `MCP Integration`.
4. Acesse `Tools > Options > MCP Integration`.
5. Ative `Enable MCP Server`, mantenha a porta `8282`, gere uma `Security Key`
   e mantenha `Secure Only` ativado.
6. Crie o arquivo `.env` na raiz a partir do exemplo:

   ```bash
   cp .env.example .env
   ```

7. Substitua o valor de `ZAP_API_KEY` pela chave gerada no ZAP.
8. No VS Code, execute `MCP: List Servers`, inicie `owasp-zap` e aceite a
   instalação do `mcp-remote` quando ela for solicitada.

O arquivo `.vscode/mcp.json` inicia o adaptador via `npx`; não é necessário
adicionar dependência ao `pom.xml` ou ao `package.json`.

## Uso com esta API

Inicie a aplicação Spring Boot e confirme que o alvo responde:

```bash
./mvnw spring-boot:run
curl http://localhost:8080/v3/api-docs
```

No Copilot Chat em modo Agent, use uma solicitação como:

> Use as ferramentas OWASP ZAP MCP para criar um contexto para
> `http://localhost:8080`, executar o spider, aguardar a conclusão, consultar
> os alertas passivos e gerar um relatório. Não execute scan ativo ainda.

Depois de revisar o resultado, um scan ativo pode ser solicitado explicitamente:

> Para o contexto já criado de `http://localhost:8080`, execute o active scan,
> aguarde a conclusão, liste os alertas por risco e gere o relatório HTML.

O scan ativo envia requisições potencialmente destrutivas. Execute-o apenas
contra esta aplicação local ou outro alvo cujo teste você esteja autorizado a
realizar. Não exponha a porta `8282` para a rede e não comite `.env`.

## Diagnóstico rápido

- O ZAP precisa estar aberto para o MCP Integration permanecer disponível.
- Se a conexão falhar, confirme que `https://localhost:8282` está ouvindo e que
  a chave do `.env` é a mesma exibida nas opções do ZAP.
- `NODE_TLS_REJECT_UNAUTHORIZED=0` é usado somente para o certificado
  autoassinado local do ZAP; não reutilize esta configuração para um endpoint
  remoto.