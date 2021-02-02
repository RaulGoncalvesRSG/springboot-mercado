package curso.springboot.util;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class ReportUtil<T> implements Serializable{

	private static final long serialVersionUID = 1L;

	//Retorna o PDF em byte para download no navegador
	//relatorio é o nome do arquivo .jasper que está na pasta de relatórios
	public byte[] gerarRelatorio(List<T> listaDados, String relatorio, ServletContext servletContext) 
				throws Exception {
		//Recebe lista (genérica) de qualquer tipo de dados. 	Cria a lista de dados para o relatório 
		JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listaDados);
		//Carrega o caminho do arquivo jasper compilado
		String caminhoJasper = servletContext.getRealPath("relatorios") + File.separator + relatorio + ".jasper";
		//Carrega o arquivo jasper passando os dados
		JasperPrint impressoraJasper = JasperFillManager.fillReport(caminhoJasper, new HashMap<>(), jrbcds);
		
		//Exporta para byte[] para fazer download do PDF
 		return JasperExportManager.exportReportToPdf(impressoraJasper);
	}
}
