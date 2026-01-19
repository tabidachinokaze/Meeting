package moe.tabidachi.meeting.mapper

interface DomainMapper<TDto, TDomain> {
    fun toDomain(dto: TDto): TDomain
}

interface DtoMapper<TDomain, TDto> {
    fun toDto(domain: TDomain): TDto
}

interface DataMapper<TDomain, TDto> : DomainMapper<TDto, TDomain>, DtoMapper<TDomain, TDto>
