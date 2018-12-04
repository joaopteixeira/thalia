package thalia.atec.thaliaPrototipo.Functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.mail.*;

import thalia.atec.thaliaPrototipo.Service.CountryRepository;
import thalia.atec.thaliaPrototipo.Service.LoginRepository;
import thalia.atec.thaliaPrototipo.Service.UserRepository;
import thalia.atec.thaliaPrototipo.Util.WebServices;
import thalia.atec.thaliaPrototipo.model.Country;
import thalia.atec.thaliaPrototipo.model.Login;
import thalia.atec.thaliaPrototipo.model.User;
import thalia.atec.thaliaPrototipo.model.Watch;
 
@Service("fuser")
public class FUser {                   //Funcoes pro USER  

	@Autowired
	UserRepository userRep;
	
	@Autowired
	LoginRepository loginRep;
	
	@Autowired
	CountryRepository countryrep;
	
	
	
	
	public FUser() {        
		super();
	}


	
	



	public String login(String email,String password) {
		
		String hash = UUID.randomUUID().toString();
		Optional<Login> login = loginRep.findByEmailAndPassword(email, password);
		if(login.isPresent()) {
			Optional<User> u = userRep.findByEmail(login.get().getEmail());
			
			//u.get().setHashes();
			u.get().getHashes().add(hash);
			userRep.save(u.get());
			return hash;
		}
		
		
		
		return null;	
	}

	public String Registry(User u,String password) {
		
		Optional<Login> userOp = loginRep.findByEmail(u.getEmail());
		
		
		
		if(userOp.isPresent()) {
			return "ja existe o email";
		}else {
			
			loginRep.save(new Login(u.getEmail(),password));
			u.setTokkensquantity(10);	
			u.setPathimage(WebServices.SERVER+"/upload/downloadFile/userdefault.png");//Quantidade Inicial de Tokkens
			u.setAccactivated(false);
			u.getWatching().add(new Watch("5bf7181088ee2d3d8be652bd", "10/24/2018 21:21:21"));
			userRep.save(u);		
			
		}
		
		
		return "Registado";
		
	}
	
	public void encryptpass(String pass) {
		
		int size = pass.length();
		
		
		String[] chars = new String[size];
		
		
		for(int i=0;i<size;i++) {
			chars[i] = String.valueOf(pass.charAt(i));
		}
		
			
		String p = "";
		for(int i=0;i<size;i++) {
			Random r = new Random();
			int num = r.nextInt(5);
			p+=chars[i]+String.valueOf(num)+UUID.randomUUID().toString().substring(0, num);
		}
		
		
		System.out.println(p);
		
		//dsycryption(p);
		
		
		
		
		
	}
	
	public void dsycryption(String pass) {
		
		int size = pass.length();
		
		
		String[] chars = new String[size];
		
		
		for(int i=0;i<size;i++) {
			chars[i] = String.valueOf(pass.charAt(i));
		}
		
		String[] passnew = new String[size];
		
		passnew[0]=chars[0];
		int i=0;
		for(int j=1;i<size-1;j++) {
			if(Integer.valueOf(chars[i+1])+i+2<size) {
				i=2+i+Integer.valueOf(chars[i+1]);
				
				passnew[j] = chars[i];
			}else {
				break;
			}
			
		}
		
		String p = "";
		for(int j=0;j<passnew.length;j++) {
			if(passnew[j]!=null)
			p+=passnew[j];
		}
		
		System.out.println(p);
		
		
	}
	
	
	
	
	public List<User> getUserContainig(String name){
		
		String firstname = "";
		String lastname = "";
		
		if(name.contains(" ")) {
			firstname = name.substring(0, name.indexOf(" "));
			lastname = name.substring(name.indexOf(" "),name.length()-1);
		}else {
			firstname = name;
			lastname = name;
		}
		
		System.out.println(firstname);
		System.out.println(lastname);
		
		
		return userRep.findByFirstnameContainingOrLastnameContaining(firstname,lastname);
		
	}
	
	public String sendEmailReset(String usermail) {
		
	
		
		Optional<Login> userOp = loginRep.findByEmail(usermail);

	
		System.out.println("User id: "+userOp.get().getId() + " reseted pass ");
		
		
		
		if(userOp.isPresent()) {

			
			Email email = new SimpleEmail();
			email.setHostName("smtp.googlemail.com");
			email.setSmtpPort(465);
			email.setAuthentication("artlinkrecovery@gmail.com", "thalia2018");
			//email.setAuthentication("admin@artlink.pt","thaliapt18");
			//email.setHostName("webdomain02.dnscpanel.com");
			//email.setHostName("pop.sapo.pt ");
			//email.setSmtpPort(110);
			//email.setAuthentication("apoio.artlink@sapo.pt", "Thalia2018");
			email.setSSL(true);
		
			
			
		try {
				
			String newPassword = UUID.randomUUID().toString();	
			
			

			
				email.setFrom("artlinkrecovery@gmail.com");
				email.setSubject("Recuperação de Password da sua conta Artlink");
				email.setMsg("A sua nova password:  "
						+ newPassword + "   :Se não efectuou este pedido por favor contacte a administração da Artlink");
				email.addTo(usermail);
				email.send();
				
				
				
				userOp.get().setPassword(newPassword);
				loginRep.save(userOp.get());
				
						
				System.out.println("email enviado para: "+ usermail +"");
			
				
			
		
			}catch(EmailException e) {
				e.printStackTrace();
			 }
			

		
		
			
		}else {
			
			return "Conta nao existe o email";		
				}
		
	return "enviado pedido";
	}
	
	
	
public String sendEmailNovaPassword(String usermail,String newpass) {
		
	
		
		Optional<Login> userOp = loginRep.findByEmail(usermail);

	
		System.out.println("User id: "+userOp.get().getId() + " reseted pass ");
		
		
		
		if(userOp.isPresent()) {

			
			Email email = new SimpleEmail();
			email.setHostName("smtp.googlemail.com");
			email.setSmtpPort(465);
			email.setAuthentication("artlinkrecovery@gmail.com", "thalia2018");
			//email.setAuthentication("admin@artlink.pt","thaliapt18");
			//email.setHostName("webdomain02.dnscpanel.com");
			//email.setHostName("pop.sapo.pt ");
			//email.setSmtpPort(110);
			//email.setAuthentication("apoio.artlink@sapo.pt", "Thalia2018");
			email.setSSL(true);
		
			
			
		try {
				
			String newPassword = newpass;	
			
			

			
				email.setFrom("artlinkrecovery@gmail.com");
				email.setSubject("Recuperação de Password da sua conta Artlink");
				email.setMsg("A sua nova password:  "
						+ newPassword + "   :Se não efectuou este pedido por favor contacte a administração da Artlink");
				email.addTo(usermail);
				email.send();
				
				
				
				userOp.get().setPassword(newPassword);
				loginRep.save(userOp.get());
				
						
				System.out.println("email enviado para: "+ usermail +"");
			
				
			
		
			}catch(EmailException e) {
				e.printStackTrace();
			 }
			

		
		
			
		}else {
			
			return "Conta nao existe o email";		
				}
		
	return "enviado pedido";
	}



	public ArrayList<Country> getCountry() {
		
		return (ArrayList<Country>) countryrep.findAll();
		
	}
	

	 
	
	
}
