package br.com.bibliotecaedt.gerenciador.agedador;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public final class Agendador {

    private static final int INTERVALO = 5;
    private static Agendador instancia;

    public static Agendador getInstancia() {
	if (instancia == null) {
	    instancia = new Agendador();
	}
	return instancia;
    }

    public void inicializa() {
	try {
	    JobDetail job = JobBuilder.newJob(BuscarNormas.class)
		    .withIdentity("buscaNormas").build();
	    Trigger trigger = TriggerBuilder
		    .newTrigger()
		    .withSchedule(
			    SimpleScheduleBuilder.simpleSchedule()
				    .withIntervalInHours(INTERVALO)
				    .repeatForever()).build();
	    SchedulerFactory schFactory = new StdSchedulerFactory();
	    Scheduler sch = schFactory.getScheduler();
	    sch.start();
	    sch.scheduleJob(job, trigger);

	} catch (SchedulerException e) {
	    System.out.println(e.getMessage());
	}
    }
}
