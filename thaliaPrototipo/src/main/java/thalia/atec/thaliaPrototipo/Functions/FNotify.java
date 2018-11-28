package thalia.atec.thaliaPrototipo.Functions;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import thalia.atec.thaliaPrototipo.Service.NotifyRepository;
import thalia.atec.thaliaPrototipo.Service.UserRepository;
import thalia.atec.thaliaPrototipo.model.Notify;
import thalia.atec.thaliaPrototipo.model.User;

@Service("fnotify")
public class FNotify {
	
	
	@Autowired
	NotifyRepository nrep;
	
	@Autowired
	UserRepository urep;
	
	
	
	public List<Notify> notify(String hash){
		
		Optional<User> user = urep.findByHashes(hash);
		
		
		if(user.isPresent()) {
			List<Notify> aux = nrep.findByUseridAndEstado(user.get().getId(),Notify.NVISTO);
			
			if(!aux.isEmpty()) {
				
				for(Notify n:aux) {
					n.setEstado(Notify.VISTO);
					nrep.save(n);
				}
				
				return aux;
			}
		}
		
		
		
		return null;
	}
	
	
	
	public void saveNotify(Notify n) {
		nrep.save(n);
	}
	

}
