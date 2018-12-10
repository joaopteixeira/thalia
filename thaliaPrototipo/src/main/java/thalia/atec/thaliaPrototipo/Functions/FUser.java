package thalia.atec.thaliaPrototipo.Functions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
	
	
	public String changePassword(String hash,String holder,String nova) {
		
		Optional<User> user = userRep.findByHashes(hash);
		
		if(user.isPresent()) {
			
			Optional<Login> login = loginRep.findByEmailAndPassword(user.get().getEmail(), holder);
			
			if(login.isPresent()) {
				
				login.get().setPassword(nova);
				
				loginRep.save(login.get());
				return "aceite";
			}else {
				return "erroholder";
			}
			
		}else {
			return "null";
		}
		
		
	}
	
	
	public List<User> getUserAdvacend(String name,String district,String category,String subcategory){
		String firstname = "";
		String lastname = "";
		
		if(name.contains(" ")) {
			firstname = name.substring(0, name.indexOf(" "));
			lastname = name.substring(name.indexOf(" "),name.length()-1);
		}else {
			firstname = name;
			lastname = name;
		}
		
		System.out.println(category);
		System.out.println(district);
		if(category.compareTo("Qualquer")==0 && district.compareTo("Qualquer")==0) {
			return userRep.findByFirstnameContainingOrLastnameContaining(firstname,lastname);
		}
		else if(category.compareTo("Qualquer")==0 && district.compareTo("Qualquer")!=0) {
			return userRep.findByDistrict(district);
		}else if(category.compareTo("Qualquer")!=0 && subcategory.compareTo("Qualquer")!=0 && district.compareTo("Qualquer")!=0) {
			return userRep.findByDistrictAndCategoryAndSubcategory(district, category, subcategory);
		}else if(category.compareTo("Qualquer")!=0 && subcategory.compareTo("Qualquer")==0 && district.compareTo("Qualquer")!=0) {
			return userRep.findByDistrictAndCategory( district, category);
		}else if(category.compareTo("Qualquer")!=0 && subcategory.compareTo("Qualquer")!=0 && district.compareTo("Qualquer")==0) {
			return userRep.findByCategoryAndSubcategory( category, subcategory);
		}else if(category.compareTo("Qualquer")!=0 && subcategory.compareTo("Qualquer")==0 && district.compareTo("Qualquer")==0) {
			return userRep.findByCategory( category);
		}
		
		return null;
		
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
	


	
	public String addWatching(String hash,String iduser) {
		
		Optional<User> user = userRep.findByHashes(hash);
		Optional<User> user2 = userRep.findById(iduser);
		
		
		if(user.isPresent() && user2.isPresent()) {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

			// Get the date today using Calendar object.
			Date today = Calendar.getInstance().getTime();        
			// Using DateFormat format method we can create a string 
			// representation of a date with the defined format.
			String reportDate = df.format(today);
			
			
			
			boolean check = false;
			boolean check1 = false;
			
			
			

			
			if(!user.get().getWatching().isEmpty() && !user2.get().getWatched().isEmpty()) {
				
				for(Watch w : user.get().getWatching()) {
					if(w.getIduser().compareTo(iduser)==0) {
						check=true;
					
					}
					if(check) {
						user.get().getWatching().remove(w);
						break;
					}
				}
				for(Watch w : user2.get().getWatched()) {
					if(w.getIduser().compareTo(user.get().getId())==0) {
						check1=true;
					
					}
					if(check1) {
						user2.get().getWatched().remove(w);
						break;
					}
				}
			}
			
			
			
			if(!check && !check1) {
				user.get().getWatching().add(new Watch(iduser, reportDate,user2.get().getFirstname()+" "+user2.get().getLastname(),user2.get().getPathimage()));
				user2.get().getWatched().add(new Watch(user.get().getId(), reportDate,user.get().getFirstname()+" "+user.get().getLastname(),user.get().getPathimage()));
				
				userRep.save(user.get());
				userRep.save(user2.get());
				
				return "aceite";
			}else {
				userRep.save(user.get());
				userRep.save(user2.get());
			}
			
			
		}
		
		return "naceite";
		
	}
	
	
	
}
