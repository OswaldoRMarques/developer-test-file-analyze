package br.com.segware;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AnalisadorRelatorio implements IAnalisadorRelatorio {

	@Override
	public Map<String, Integer> getTotalEventosCliente() {
		String csvRelatorio = "/Users/oswaldormarques/GiovaniCani/git/developer-test-file-analyze/src/test/java/br/com/segware/relatorio.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		Map<String, Integer> totalEventosCliente = new HashMap<String, Integer>();
		
		try {
			br = new BufferedReader(new FileReader(csvRelatorio));				
			line = br.readLine();
			String[] evento = line.split(cvsSplitBy);
			totalEventosCliente.put(evento[1], 1);
			while ((line = br.readLine()) != null) {
				evento = line.split(cvsSplitBy);
				Integer total = 1;
				Integer eventoCliente = totalEventosCliente.get(evento[1]);
				if(eventoCliente != null) {
					total += eventoCliente;
				}
				totalEventosCliente.put(evento[1], total);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return totalEventosCliente;
	}

	@Override
	public Map<String, Long> getTempoMedioAtendimentoAtendente() {
		String csvRelatorio = "/Users/oswaldormarques/GiovaniCani/git/developer-test-file-analyze/src/test/java/br/com/segware/relatorio.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		Map<String, Long> tempoMedioAtendimentoAtendente = new HashMap<String, Long>();
		
		try {
			br = new BufferedReader(new FileReader(csvRelatorio));				
			line = br.readLine();
			String[] atendimento = line.split(cvsSplitBy);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dataInicio = formatter.parse(atendimento[4]);
			Date dataFim = formatter.parse(atendimento[5]);
			Long tempoMedio = (dataFim.getTime() - dataInicio.getTime());
			tempoMedio = TimeUnit.MILLISECONDS.toSeconds(tempoMedio);
			tempoMedioAtendimentoAtendente.put(atendimento[6], tempoMedio);
			while ((line = br.readLine()) != null) {
				atendimento = line.split(cvsSplitBy);
				dataInicio = formatter.parse(atendimento[4]);
				dataFim = formatter.parse(atendimento[5]);
				tempoMedio = (dataFim.getTime() - dataInicio.getTime());
				tempoMedio = TimeUnit.MILLISECONDS.toSeconds(tempoMedio);
				Long tempoMedioAtendimento = tempoMedioAtendimentoAtendente.get(atendimento[6]);
				if (tempoMedioAtendimento != null) {
					tempoMedio += tempoMedioAtendimento;
					tempoMedioAtendimentoAtendente.put(atendimento[6], tempoMedio);
				} else {
					tempoMedioAtendimentoAtendente.put(atendimento[6], tempoMedio);
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return tempoMedioAtendimentoAtendente;
	}

	@Override
	public List<Tipo> getTiposOrdenadosNumerosEventosDecrescente() {
		String csvRelatorio = "/Users/oswaldormarques/GiovaniCani/git/developer-test-file-analyze/src/test/java/br/com/segware/relatorio.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		List<Tipo> tiposOrdenadosNumerosEventosDecrescente = new ArrayList<Tipo>();
		Map<Tipo, Integer> tiposQuantidadeOcorrencias = new HashMap<Tipo, Integer>();
		
		try {
			br = new BufferedReader(new FileReader(csvRelatorio));
			line = br.readLine();
			String[] ocorrencia = line.split(cvsSplitBy);
			tiposQuantidadeOcorrencias.put(Tipo.valueOf(ocorrencia[3]), 1);
			while ((line = br.readLine()) != null) {
				ocorrencia = line.split(cvsSplitBy);
				Integer total = 1;
				Integer tipoQuantidadeOcorrencia = tiposQuantidadeOcorrencias.get(Tipo.valueOf(ocorrencia[3]));
				if (tipoQuantidadeOcorrencia != null) {
					total += tipoQuantidadeOcorrencia;
				}
				tiposQuantidadeOcorrencias.put(Tipo.valueOf(ocorrencia[3]), total);
			}
			Map<Tipo, Integer> tiposQuantidadeOcorrenciasOrdenado = sortByValue(tiposQuantidadeOcorrencias);
			tiposOrdenadosNumerosEventosDecrescente = new ArrayList<Tipo>(tiposQuantidadeOcorrenciasOrdenado.keySet());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return tiposOrdenadosNumerosEventosDecrescente;
	}
	
	private static Map<Tipo, Integer> sortByValue(Map<Tipo, Integer> unsortedMap) {

		List<Map.Entry<Tipo, Integer>> list = 
			new LinkedList<Map.Entry<Tipo, Integer>>(unsortedMap.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<Tipo, Integer>>() {
			public int compare(Map.Entry<Tipo, Integer> o1,
                                           Map.Entry<Tipo, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<Tipo, Integer> sortedMap = new LinkedHashMap<Tipo, Integer>();
		for (Iterator<Map.Entry<Tipo, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<Tipo, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	@Override
	public List<Integer> getCodigoSequencialEventosDesarmeAposAlarme() {
		String csvRelatorio = "/Users/oswaldormarques/GiovaniCani/git/developer-test-file-analyze/src/test/java/br/com/segware/relatorio.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		List<Integer> codigoSecretoSequencialEventosDesarmeAposAlarme = new ArrayList<Integer>();
		
		try {
			br = new BufferedReader(new FileReader(csvRelatorio));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dataInicioAlarme = new Date();
			Date dataInicioDesarme = new Date();
			Long eventoDesarme = TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES);
			Integer codCliente = 0;
			
			while ((line = br.readLine()) != null) {
				String[] evento = line.split(cvsSplitBy);
				if (Tipo.valueOf(evento[3]) == Tipo.ALARME) {
					dataInicioAlarme = formatter.parse(evento[4]);
					codCliente = Integer.parseInt(evento[1]);
				} else if (Tipo.valueOf(evento[3]) == Tipo.DESARME && Integer.parseInt(evento[1]) == codCliente) {
					dataInicioDesarme = formatter.parse(evento[4]);
					Long duracao = dataInicioDesarme.getTime() - dataInicioAlarme.getTime();
					if (duracao <= eventoDesarme) {
						codigoSecretoSequencialEventosDesarmeAposAlarme.add(Integer.parseInt(evento[0]));
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return codigoSecretoSequencialEventosDesarmeAposAlarme;
	}

}
