package es.unizar.tmdad.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import es.unizar.tmdad.app.service.FacebookStreamService;

@Configuration
@EnableScheduling
public class FacebookFlow implements SchedulingConfigurer
{
   @Bean
   public FacebookStreamService timedThingy()
   {
      return new FacebookStreamService();
   }

  @Bean()
  public ThreadPoolTaskScheduler taskScheduler() {
     return new ThreadPoolTaskScheduler();
  }

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar)
  {
      taskRegistrar.setTaskScheduler(taskScheduler());
      taskRegistrar.addFixedRateTask(new Runnable()
      {
         public void run()
         {
            timedThingy().sendPost();
         }
      }, 15000);
  }
}
