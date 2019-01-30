package com.ebeijia.zl.service.control.quartz.conf;
import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import com.ebeijia.zl.service.control.quartz.SpecAccountTypeBizJob;
import com.ebeijia.zl.service.control.quartz.TelePhoneRechargeBizJob;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.ebeijia.zl.service.control.quartz.AccessTokenBizJob;
import com.ebeijia.zl.service.control.quartz.BaseDictBizJob;

@Configuration
public class QuartzConfigure {
	// 配置文件路径
	private static final String QUARTZ_CONFIG = "/conf/quartz.properties";
	
	@Autowired
    @Qualifier(value = "dataSource")
    private DataSource dataSource;
	
	@Value("${quartz.cronExpression.dictBiz}")
	private String dictBizExpression;

    @Value("${quartz.cronExpression.jsToken}")
    private String jsTokenExpression;

    @Value("${quartz.cronExpression.accessToken}")
    private String accessTokenExpression;

    @Value("${quartz.cronExpression.teleRecharge}")
    private String teleRechargeExpression;

    @Value("${quartz.cronExpression.billingType}")
    private String billingTypeExpression;

	/**
	 * 从quartz.properties文件中读取Quartz配置属性
	 * @return
	 * @throws IOException
	 */
    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource(QUARTZ_CONFIG));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }
    
    /**
     * JobFactory与schedulerFactoryBean中的JobFactory相互依赖,注意bean的名称
     * 在这里为JobFactory注入了Spring上下文
     * 
     * @param applicationContext
     * @return
     */
    @Bean
    public JobFactory buttonJobFactory(ApplicationContext applicationContext) {
    	AutoWiredSpringBeanToJobFactory jobFactory = new AutoWiredSpringBeanToJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    /**
     * 
     * @param buttonJobFactory  为SchedulerFactory配置JobFactory
     * @param cronJobTrigger  
     * @return
     * @throws IOException
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory buttonJobFactory, Trigger... cronJobTrigger) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(buttonJobFactory);
        factory.setOverwriteExistingJobs(true);
        factory.setAutoStartup(true); // 设置自行启动
        factory.setQuartzProperties(quartzProperties());
        factory.setTriggers(cronJobTrigger);
        factory.setDataSource(dataSource);// 使用应用的dataSource替换quartz的dataSource
        return factory;
    }

    /**
     * 配置JobDetailFactory
     * JobDetailFactoryBean与CronTriggerFactoryBean相互依赖,注意bean的名称
     * 
     * @return
     */
    @Bean
    public JobDetailFactoryBean baseDictBizDetail() {
         //集群模式下必须使用JobDetailFactoryBean，MethodInvokingJobDetailFactoryBean 类中的 methodInvoking 方法，是不支持序列化的
         JobDetailFactoryBean jobDetail = new JobDetailFactoryBean();
         jobDetail.setDurability(true); 
         jobDetail.setRequestsRecovery(true);
         jobDetail.setJobClass(BaseDictBizJob.class); 
         return jobDetail; 
    }
    
    /**
     * 配置具体执行规则
     * @param baseDictBizDetail
     * @return
     */
    @Bean
    public CronTriggerFactoryBean baseDictBizJobTrigger(JobDetail baseDictBizDetail) {
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        tigger.setJobDetail(baseDictBizDetail);
        tigger.setStartDelay(2000);   //延迟启动
        tigger.setCronExpression(dictBizExpression);  //从application.yml文件读取
        return tigger;
    }
    
    
    
    /**
     * 配置JobDetailFactory
     * JobDetailFactoryBean与CronTriggerFactoryBean相互依赖,注意bean的名称
     * 
     * @return
     */
    @Bean
    public JobDetailFactoryBean accessTokenBizDetail() {
         //集群模式下必须使用JobDetailFactoryBean，MethodInvokingJobDetailFactoryBean 类中的 methodInvoking 方法，是不支持序列化的
         JobDetailFactoryBean jobDetail = new JobDetailFactoryBean();
         jobDetail.setDurability(true); 
         jobDetail.setRequestsRecovery(true);
         jobDetail.setJobClass(AccessTokenBizJob.class); 
         return jobDetail; 
    }
    
    /**
     * 配置具体执行规则
     * @param accessTokenBizDetail
     * @return
     */
    @Bean
    public CronTriggerFactoryBean accessTokenBizTrigger(JobDetail accessTokenBizDetail) {
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        tigger.setJobDetail(accessTokenBizDetail);
        tigger.setStartDelay(2000);   //延迟启动
        tigger.setCronExpression(accessTokenExpression);  //从application.yml文件读取
        return tigger;
    }


    /**
     * 配置JobDetailFactory
     * JobDetailFactoryBean与CronTriggerFactoryBean相互依赖,注意bean的名称
     *
     * @return
     */
    @Bean
    public JobDetailFactoryBean specAccountTypeDetail() {
        //集群模式下必须使用JobDetailFactoryBean，MethodInvokingJobDetailFactoryBean 类中的 methodInvoking 方法，是不支持序列化的
        JobDetailFactoryBean jobDetail = new JobDetailFactoryBean();
        jobDetail.setDurability(true);
        jobDetail.setRequestsRecovery(true);
        jobDetail.setJobClass(SpecAccountTypeBizJob.class);
        return jobDetail;
    }

    /**
     * 配置具体执行规则
     * @param specAccountTypeDetail
     * @return
     */
    @Bean
    public CronTriggerFactoryBean specAccountTypeTrigger(JobDetail specAccountTypeDetail) {
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        tigger.setJobDetail(specAccountTypeDetail);
        tigger.setStartDelay(2000);   //延迟启动
        tigger.setCronExpression(billingTypeExpression);  //从application.yml文件读取
        return tigger;
    }

    /**
     * 配置JobDetailFactory
     * JobDetailFactoryBean与CronTriggerFactoryBean相互依赖,注意bean的名称
     *
     * @return
     */
    @Bean
    public JobDetailFactoryBean telePhoneRechargeBiz() {
        JobDetailFactoryBean jobDetail = new JobDetailFactoryBean();
        jobDetail.setDurability(true);
        jobDetail.setRequestsRecovery(true);
        jobDetail.setJobClass(TelePhoneRechargeBizJob.class);
        return jobDetail;
    }

    /**
     * 配置具体执行规则
     * @param specAccountTypeDetail
     * @return
     */
    @Bean
    public CronTriggerFactoryBean telePhoneRechargeBizTrigger(JobDetail telePhoneRechargeBiz) {
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        tigger.setJobDetail(telePhoneRechargeBiz);
        tigger.setStartDelay(2000);   //延迟启动
        tigger.setCronExpression(teleRechargeExpression);  //从application.yml文件读取
        return tigger;
    }
}