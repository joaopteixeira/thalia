package thalia.atec.thaliaPrototipo.Controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import thalia.atec.thaliaPrototipo.Functions.FCategories;
import thalia.atec.thaliaPrototipo.Functions.FCategory;
import thalia.atec.thaliaPrototipo.Functions.FFeed;
import thalia.atec.thaliaPrototipo.Functions.FPost;
import thalia.atec.thaliaPrototipo.Functions.FUser;
import thalia.atec.thaliaPrototipo.Service.CategoryRepository;
import thalia.atec.thaliaPrototipo.Service.FileStorageService;
import thalia.atec.thaliaPrototipo.Service.UserRepository;
import thalia.atec.thaliaPrototipo.Util.DateUtil;
import thalia.atec.thaliaPrototipo.model.Category;
import thalia.atec.thaliaPrototipo.model.Comment;
import thalia.atec.thaliaPrototipo.model.Media;
import thalia.atec.thaliaPrototipo.model.Post;
import thalia.atec.thaliaPrototipo.model.User;

@Controller
public class WebHome {
	
	@Autowired
	FPost  fpost;
	
	@Autowired
	FUser  fuser;
	
	@Autowired
	UserRepository userrep;;
	
	@Autowired
	CategoryRepository catrep;
	@Autowired
	FFeed ffeed;
	
	 @Autowired
	 private FileStorageService fileStorageService;
	
	@GetMapping("/index")
	public String index() {
		

		return "index.html";
	}
	
	@GetMapping("/feed")
	public String feed(Model page,@RequestParam("main") String main,@RequestParam("frag") String frag,@RequestParam(value="keyword",defaultValue="*") String keyword,HttpSession session){
		

		
		User u = (User)session.getAttribute("User");
		
		Optional<User> user = userrep.findById(u.getId());
		
	
		if (user.isPresent()) {

			page.addAttribute("User", user.get());
			page.addAttribute("main", main);
			page.addAttribute("frag", frag);
			session.setAttribute("User", user.get());
			page.addAttribute("friends", ffeed.getFriends(user.get().getId()));

			if (main.compareTo("perfil") == 0) {

				if (frag.compareTo("timeline") == 0) {

					
					page.addAttribute("User", (User) session.getAttribute("User"));
					page.addAttribute("posts",fpost.getPostsByUser(u.getId() ) );
					page.addAttribute("frag", "timeline");

					return "feedmain.html";

				}
				
				if (frag.compareTo("about") == 0) {

			
					page.addAttribute("frag", "about");

					return "feedmain.html";

				}
				
				if (frag.compareTo("galeria") == 0) {

				
					page.addAttribute("frag", "galeria");

					return "feedmain.html";

				}
				
				if (frag.compareTo("playlist") == 0) {

			
					page.addAttribute("frag", "playlist");

					return "feedmain.html";

				}
				
				if (frag.compareTo("amigos") == 0) {

				
					page.addAttribute("frag", "amigos");

					return "feedmain.html";

				}
				
				if (frag.compareTo("profile") == 0) {

			
					page.addAttribute("frag", "profile");

					return "feedmain.html";

				}
				
	

			}

			if (main.compareTo("homepage") == 0) {

				if (frag.compareTo("post") == 0) {

					page.addAttribute("frag", "post");
					System.out.println(user.get().getId());
					page.addAttribute("posts", fpost.getPost(user.get().getId(), 0, 0));

					return "feedmain.html";

				}

				if (frag.compareTo("chat") == 0) {

					page.addAttribute("frag", "chat");
					page.addAttribute("User", (User) session.getAttribute("User"));
					return "feedmain.html";

				}

				if (frag.compareTo("contacts") == 0) {

					page.addAttribute("frag", "contacts");
					page.addAttribute("User", (User) session.getAttribute("User"));
					return "feedmain.html";

				}

				if (frag.compareTo("search") == 0) {

			
					List<Category> categories = catrep.findAll();
					page.addAttribute("categories", categories);
					
				
				
					
					if(keyword.compareTo("*")!=0) {
						
						List<User> results = fuser.getUserContainig(keyword);

						page.addAttribute("results",results);
					}

				}

				return "feedmain.html";

			}
		}

		return "redirect:/index";

	}
	
	@PostMapping("/newpost")
  public String feed(@RequestParam("file") MultipartFile file,@RequestParam("content") String content,@RequestParam("typemedia") int typemedia,/*@RequestParam("pathfile") String pathfile,*/ HttpSession session){
//		
//		
		String fileName = fileStorageService.storeFile(file);
    	//String fileName = rand.replace("-", "");

        
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("upload/downloadFile/")
                .path(fileName)
                .toUriString();     
        System.out.println(fileDownloadUri);
		
		
		User u = (User)session.getAttribute("User");
//		
		Post p = new Post();
//		
//		
		LocalDateTime date =  LocalDateTime.now();  
//
//		
			String username = u.getUsername() ;
//		
			String iduser;
//		
			Media media = new Media(typemedia,fileDownloadUri);//pathfile);
//		
//		
	    p.setContent(content);
		p.setUsername(username);
		p.setIduser(u.getId());
		p.setDate(date.toString());	
		p.setMedia(media);
		fpost.newPost(p);
		
//		
	
	 return "redirect:/feed?main=homepage&frag=post";
	}
//	
	
	
@PostMapping("/newcomment")
public String newComment(@RequestParam("content") String content,@RequestParam("idpost") String idpost, HttpSession session){
	
	System.out.println("Id-POST:  "+idpost);
	System.out.println("Content:  "+content);
	
	User u = (User)session.getAttribute("User");
	
	String hash = (String)session.getAttribute("hash");
	System.out.println("HASH:  "+hash);

	Post p = fpost.addComment(idpost,hash,content);

	
	
	
  return "redirect:/feed?main=homepage&frag=post";
}


@PostMapping("/newsearch")	
public String NewSearch(Model page,@RequestParam("keyword") String keyword, HttpSession session) {
	
	
	page.addAttribute("User",(User)session.getAttribute("User"));

	

	  return "redirect:/feed?main=homepage&frag=search&keyword="+keyword;
}




}
