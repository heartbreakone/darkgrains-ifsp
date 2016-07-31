
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Darkgrains - Cadastro</title>
  <meta name="keywords" content="" />
  <meta name="description" content="" />
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
  <link href="menufixo.css" rel="stylesheet" type="text/css">
  <link href="css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css">
  <link href="estiloprincipal.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="css/superslides.css">
    <link rel="stylesheet" href="css/normalize.css">
        <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/normalize.css">
        <link rel="stylesheet" href="css/main.css">

  <link rel="alternate" type="application/json+oembed"
</head>
<body>
   <header>
        <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand" href="index.html"><img src="images/logo.png"width="288" height="167" /></a>
          <nav class="nav-collapse collapse">
            <ul class="nav pull-right">
            <li><a href="downloadpage.html">Jogue agora!</a></li>
                             <li><a href="cadastro.html">Cadastro</a></li>
              </li>
              <li><a href="rec.html">RecuperaÃ§Ã£o de Senha</a></li>
              <li><a href="sobre.html">Sobre</a></li>
                 
              
             
            </ul>
          </nav>
        </div>
      </div>
    </div>
    </div>
  </header>
  <body>
  <div class="sticky-container">
  <style>
   .sticky-container{
		/*background-color: #333;*/
		padding: 0px;
		margin: 0px;
		position: fixed;
		right: -119px;
		top:130px;
		width: 200px;
z-index:99999;
	}
</style>
    <ul class="sticky">
    <li><a href="http://darkgrains.wordpress.com/"><img width="32" height="32" title="" alt="" src="images/fb1.png" />
  <p>Wordpress</p>
    </li></a>

    <li><a href="https://www.youtube.com/user/darkgrains"> <img width="32" height="32" title="" alt="" src="images/pin1.png" />
      <p>YouTube</p>
      
</li></a>
   
   </style>
    </ul>
</div>



  <script type="text/javascript">
    $('.dropdown-menu').click(function(e) {
        e.stopPropagation();
    });
</script>

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
  <script src="javascripts/jquery.easing.1.3.js"></script>
  <script src="javascripts/jquery.animate-enhanced.min.js"></script>
  <script src="javascripts/jquery.superslides.js" type="text/javascript" charset="utf-8"></script>

  

 <script type="text/javascript">
 var clickmessage="Proibido copiar de Darkgrains!"
document.oncontextmenu = function(e) {
    e = e || window.event;
	
    if (/^img$/i.test((e.target || e.srcElement).nodeName)){
		
	return false;
	}
	
};
</script> 





        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="js/vendor/jquery-1.9.1.min.js"><\/script>')</script>
        <script src="js/imagesloaded.js"></script>
        <script src="js/skrollr.js"></script>
        <script src="js/_main.js"></script>




    
    
  <script src="js/bootstrap.min.js"></script>
  <script>
$.noConflict();
</script>
<script type="text/javascript">
    $('.dropdown-menu').click(function(e) {
        e.stopPropagation();
    });
</script>

<style> 
    footer {
        margin-top:100px;
    }
    </style>
<footer>
    <h2><center>
        <%
            String output;
            String name = request.getParameter("user");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            if(name.length() < 5) {
            output =  "O nome precisa conter, no mínimo, 5 caracteres.";
        }
        if(name.length() > 45) {
            output =  "O nome pode conter, no no máximo, 45 caracteres.";
        }
        if(password.length() < 4) {
            output =   "A senha precisa conter, no mínimo, 4 caracteres.";
        }
        if(password.length() > 45) {
            output =   "A senha pode conter, no no máximo, 45 caracteres.";
        }
        if(!email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
            output =   "O endereço de email não é válido.";
        }
        if(userExists(name)) {
            return new Response(false, "Usuário já registrado.");
        }
        if (emailExists(email)) {
            return new Response(false, "Email já registrado.");
        }
        
        try {
            PreparedStatement stmt = c.prepareStatement("insert into player values (NULL,?,?,?)");
            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.executeUpdate();
            
            return new Response(true, "");
        } catch (SQLException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(false, "Houve um erro inesperado.");
        }
            
        %>
            <form action="cad.asp" method="post">
                UsuÃ¡rio: <input type="text" name="user"><br>
                Senha <input type="password" name="senha"><br>
                ConfirmaÃ§Ã£o de senha: <input type="password" name="senha2"><br>
                E-mail: <input type="text" name="email"><br>
                <input type="submit" value="Cadastrar">
            </form>
    </center></h2>
  </footer>

<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="js/bootstrap.min.js"></script>
<script type="text/javascript">
    $('.dropdown-menu').click(function(e) {
        e.stopPropagation();
    });
</script>

</p>
</body>
</html>