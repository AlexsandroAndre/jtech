# Task Manager App

Aplicação de gerenciamento de tarefas desenvolvida com Vue 3, TypeScript, Pinia e Vuetify.

## Requisitos

- Node.js 18+ 
- npm ou yarn
- API Backend rodando em http://localhost:8080

## Instalação

1. Extraia o arquivo ZIP do projeto

2. Instale as dependências:
```bash
npm install
```

## Executar o Projeto

### Modo Desenvolvimento
```bash
npm run dev
```
A aplicação estará disponível em http://localhost:5173

### Build para Produção
```bash
npm run build
```

### Preview da Build
```bash
npm run preview
```

## Executar Testes

### Rodar todos os testes
```bash
npm run test
```

### Rodar testes com UI
```bash
npm run test:ui
```

## Estrutura do Projeto

```
task-manager-app/
├── src/
│   ├── api/              # Configuração e chamadas da API
│   ├── assets/           # Recursos estáticos
│   ├── components/       # Componentes Vue reutilizáveis
│   ├── views/            # Páginas/Views da aplicação
│   ├── stores/           # Stores Pinia (gerenciamento de estado)
│   ├── router/           # Configuração de rotas
│   ├── types/            # Definições TypeScript
│   ├── styles/           # Estilos globais
│   ├── App.vue           # Componente raiz
│   └── main.ts           # Entry point
├── tests/                # Testes unitários
└── public/               # Arquivos públicos estáticos
```

## Funcionalidades

- Autenticação com JWT
- Registro de novos usuários
- Gerenciamento de múltiplas listas de tarefas
- CRUD completo de tarefas
- Marcar tarefas como concluídas
- Validação de duplicatas
- Persistência de estado
- Proteção de rotas
- Interface Material Design com paleta Mercado Livre

## Tecnologias

- Vue 3 (Composition API)
- TypeScript
- Pinia (State Management)
- Vue Router 4
- Vuetify 3 (Material Design)
- Axios (HTTP Client)
- Vitest (Testing)

## Configuração da API

A URL base da API está configurada em `src/api/axios.config.ts`:
```typescript
const API_BASE_URL = 'http://localhost:8080'
```

Certifique-se de que o backend está rodando antes de iniciar a aplicação.

## Paleta de Cores (Mercado Livre)

- Amarelo Principal: #FFE600
- Azul Principal: #3483FA
- Azul Escuro: #2968C8
- Cinza: #666666

## Suporte

Para dúvidas ou problemas, consulte a documentação do Vue 3 e Vuetify.
