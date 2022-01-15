package com.hotel.reservation.configuration

import com.hotel.reservation.converter.StringToRoomTypeConverter
import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry

@Configuration
class MvcConfig : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(StringToRoomTypeConverter())
    }

    @Bean fun modelMapper(): ModelMapper = ModelMapper()
}