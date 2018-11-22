package thalia.atec.thaliaPrototipo.Functions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import thalia.atec.thaliaPrototipo.Service.PostRepository;
import thalia.atec.thaliaPrototipo.Service.UserRepository;
import thalia.atec.thaliaPrototipo.model.Post;
import thalia.atec.thaliaPrototipo.model.User;
import thalia.atec.thaliaPrototipo.model.Watch;
@Service("fpost")
public class FPost {
	
	
	@Autowired
	PostRepository prep;
	
	@Autowired
	UserRepository urep;
	
	
	
	
	 public String newPost(Post post){
		 
		 Optional<User> u = urep.findById(post.getIduser());
			boolean check=false;
			if(u.isPresent()) {
				
						check=true;
			}
			
			if(check) {
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

				// Get the date today using Calendar object.
				Date today = Calendar.getInstance().getTime();        
				// Using DateFormat format method we can create a string 
				// representation of a date with the defined format.
				String reportDate = df.format(today);

				post.setDate(reportDate);
				
				prep.save(post);
				return "adicionado";
			}
		
		return "nadicionado";		 
	}
	 
	 
	 public List<Post> getPost(String iduser,int page){
		 
		 
		 Optional<Post> watching = prep.findById(iduser);
		 ArrayList<Post> aux = new ArrayList<>();
		 
		 if(watching.isPresent()) {
			 
<<<<<<< Updated upstream
		/*	 for(Watch w:watching.get().getIduser().getClas) {
=======
			/* for(Watch w:watching.get().getId()) {
>>>>>>> Stashed changes
				 
				 if(aux.size()>20) {
					 break;
				 }
				 for(Post p:prep.findByCreatorId(w.getIduser().getIduser().PageRequest.of(1, 1))) {
					 aux.add(p);
				 }
<<<<<<< Updated upstream
				 
=======
				
>>>>>>> Stashed changes
			 } */
			 
		 }
		 
		 
		 
		 
		 return prep.findAll();
	 }
	 
	 
	 
	 
	 
	public void like(String id_user,String  id_post) {
		
		System.out.println(id_user+"         ::   "+id_post);
		Optional<Post> p = prep.findById(id_post);
		Optional<User> u = urep.findById(id_user);
				
		boolean check = false;
		
		if(p.isPresent()) {
			System.out.println("p : presente");
			for(String ids:p.get().getLikes()) {
				if(ids.compareTo(id_user)==0) {
					System.out.println("id_user presente");
					check=true;
				}
			}
			
			if(!check && u.isPresent()) {
				p.get().getLikes().add(id_user);
				
				
			}else if(u.isPresent()) {
				p.get().getLikes().remove(id_user);
			}
			
			prep.save(p.get());
		}
		
		
		
		
	}
	 
	 
	 

}
