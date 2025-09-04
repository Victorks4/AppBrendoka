# 🍞 DOKABREN - App de Padaria

[![Android](https://img.shields.io/badge/Android-24+-green.svg)](https://developer.android.com/)
[![Firebase](https://img.shields.io/badge/Firebase-9.0+-orange.svg)](https://firebase.google.com/)
[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)

## 📱 Sobre o Projeto

**DOKABREN** é um aplicativo Android completo para gerenciamento de padaria, desenvolvido em Java com integração ao Firebase. O app oferece uma solução completa para clientes fazerem pedidos online e para administradores gerenciarem produtos, pedidos e relatórios.

### ✨ Funcionalidades Principais

#### 👤 **Para Clientes:**
- 🔐 **Autenticação completa** - Login, cadastro e recuperação de senha
- 🏠 **Tela inicial** com banners promocionais e categorias de produtos
- 🛍️ **Catálogo de produtos** organizado por categorias
- 🔍 **Sistema de busca** de produtos
- 🛒 **Carrinho de compras** com gestão de quantidades
- 📍 **Gerenciamento de endereços** para entrega
- 💳 **Checkout** com múltiplas formas de pagamento (PIX, Cartão, Dinheiro)
- 📋 **Histórico de pedidos** com status em tempo real
- 👤 **Perfil do usuário** com dados editáveis

#### 👨‍💼 **Para Administradores:**
- 📊 **Painel administrativo** com dashboard completo
- 📦 **Gestão de produtos** (adicionar, editar, remover)
- 📋 **Gestão de pedidos** com atualização de status
- 📈 **Relatórios** de vendas e análise de dados
- 📊 **Estatísticas** em tempo real

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 17
- **Plataforma:** Android (API 24+)
- **Backend:** Firebase
  - Firebase Authentication
  - Firestore Database
  - Realtime Database
  - Analytics
- **UI/UX:** Material Design 3
- **Bibliotecas:**
  - ViewPager2 para banners
  - RecyclerView para listas
  - Picasso para carregamento de imagens
  - Gson para JSON
  - OkHttp para requisições HTTP

## 📁 Estrutura do Projeto

```
app/src/main/java/com/example/appproject05/
├── 📱 Activities/
│   ├── MainActivity.java              # Tela de splash
│   ├── TelaLogin.java                 # Autenticação
│   ├── TelaCadastro.java              # Cadastro de usuário
│   ├── TelaPrincipal.java             # Activity principal
│   ├── CheckoutActivity.java          # Finalização de compra
│   ├── EditarPerfilActivity.java      # Edição de perfil
│   └── AdminPanelActivity.java        # Painel administrativo
├── 🧩 Fragments/
│   ├── HomeFragment.java              # Tela inicial
│   ├── CartFragment.java              # Carrinho
│   ├── PedidosFragment.java           # Pedidos
│   ├── PerfilFragment.java            # Perfil
│   ├── BuscaFragment.java             # Busca
│   └── ConfiguracoesFragment.java     # Configurações
├── 📦 Models/
│   ├── Product.java                   # Modelo de produto
│   ├── Order.java                     # Modelo de pedido
│   ├── User.java                      # Modelo de usuário
│   ├── CartItem.java                  # Item do carrinho
│   └── Address.java                   # Endereço
├── 🔧 Adapters/
│   ├── ProductAdapter.java            # Adapter de produtos
│   ├── CartAdapter.java               # Adapter do carrinho
│   ├── OrderAdapter.java              # Adapter de pedidos
│   └── BannerAdapter.java             # Adapter de banners
├── 🔥 Firebank/
│   ├── Firebank.java                  # Classe principal do Firebase
│   └── FireConnected.java             # Conexão com Firebase
└── 🛠️ Utils/
    ├── CartManager.java               # Gerenciador do carrinho
    └── AdminDashboardManager.java     # Gerenciador do dashboard
```

## 🚀 Como Executar

### Pré-requisitos

- Android Studio Arctic Fox ou superior
- JDK 17
- Dispositivo Android ou emulador (API 24+)
- Conta no Firebase

### 📋 Passos para Configuração

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/seu-usuario/AppBrendoka.git
   cd AppBrendoka
   ```

2. **Configure o Firebase:**
   - Crie um projeto no [Firebase Console](https://console.firebase.google.com/)
   - Baixe o arquivo `google-services.json`
   - Coloque-o na pasta `app/`

3. **Configure as dependências:**
   - Abra o projeto no Android Studio
   - Sincronize o Gradle
   - Aguarde o download das dependências

4. **Execute o projeto:**
   - Conecte um dispositivo Android ou inicie um emulador
   - Clique em "Run" no Android Studio

## 🔧 Configuração do Firebase

### 1. Autenticação
- Habilite a autenticação por email/senha no Firebase Console
- Configure as regras de segurança

### 2. Firestore Database
- Crie uma coleção `products` para os produtos
- Crie uma coleção `orders` para os pedidos
- Crie uma coleção `users` para os usuários

### 3. Storage (opcional)
- Configure o Firebase Storage para imagens dos produtos

## 📱 Telas do Aplicativo

### Tela de Login
- Interface limpa e moderna
- Campos para email e senha
- Links para cadastro e recuperação de senha

### Tela Principal
- Bottom Navigation com 5 seções
- Banners promocionais com ViewPager2
- Categorias de produtos
- Grid de produtos em destaque

### Carrinho
- Lista de itens adicionados
- Controles de quantidade
- Cálculo automático do total
- Botão para finalizar compra

### Checkout
- Seleção de endereço
- Escolha da forma de pagamento
- Resumo do pedido
- Confirmação final

## 🎨 Design e UX

- **Material Design 3** para interface moderna
- **Animações suaves** entre telas
- **Cores consistentes** com tema da padaria
- **Ícones intuitivos** para melhor usabilidade
- **Layout responsivo** para diferentes tamanhos de tela

## 🔒 Segurança

- **Autenticação Firebase** para login seguro
- **Validação de dados** em formulários
- **Regras de segurança** no Firestore
- **Criptografia** de dados sensíveis

## 📊 Funcionalidades Administrativas

### Dashboard
- Visão geral das vendas
- Gráficos de performance
- Estatísticas em tempo real

### Gestão de Produtos
- Adicionar novos produtos
- Editar produtos existentes
- Ativar/desativar produtos
- Upload de imagens

### Gestão de Pedidos
- Visualizar todos os pedidos
- Atualizar status de entrega
- Histórico completo
- Filtros por data/status

## 🤝 Contribuição

1. Faça um Fork do projeto
2. Crie uma Branch para sua Feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a Branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👨‍💻 Autor

**Seu Nome**
- GitHub: [@seu-usuario](https://github.com/seu-usuario)
- LinkedIn: [Seu LinkedIn](https://linkedin.com/in/seu-perfil)

## 🙏 Agradecimentos

- Firebase pela plataforma de backend
- Material Design pela biblioteca de componentes
- Comunidade Android pelo suporte

## 📞 Suporte

Se você encontrar algum problema ou tiver dúvidas, abra uma [issue](https://github.com/seu-usuario/AppBrendoka/issues) no GitHub.

---

⭐ **Se este projeto te ajudou, deixe uma estrela!** ⭐
