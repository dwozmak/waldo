package dwoz.waldo.web.services;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dwoz.waldo.extractor.dao.WaldoContentBean;
import dwoz.waldo.logic.ContentsLogic;

@Path("/contents")
public class WaldoPhotoService  {

	private Logger logger = LoggerFactory.getLogger(WaldoPhotoService.class);

	@GET
	@Path("/getContents")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response doGet(@Context UriInfo uriInfo){
		WaldoServicePayload<WaldoContentBean> payload = new WaldoServicePayload<>();
		
		payload.setResult(this.getContents());

		return Response.ok(payload).build();
	}
	
	private List<WaldoContentBean> getContents(){
		
		ContentsLogic logic = new ContentsLogic();
		return logic.getContents();
		
	}
	
}
