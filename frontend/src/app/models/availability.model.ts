export interface AvailabilityInModel {
  params: AvailabilityInParamsModel,
  options?: {
    fareType?: string[],
    fromCache?: boolean
  }
}

export interface AvailabilityInParamsModel {
    cabinClass: 'E' | 'B' | 'F',
    market: string,
    departureDate: string[],
    returnDate?: string,
    origin: string[],
    destination: string[],
    tripType: 'R' | 'O',
    adt: number
}

export interface AvailabilityOutModel {
  status?: string,
  errors?: number | null | string[],
  data?: any
}

export interface BookerProxyModel {
  country: string,
  language: string,
  origin: string,
  destination: string,
  departure: string,
  arrival: string,
  adult: number,
  senior: number,
  child: number,
  infant: number,
  partner: string
  class: 'E' | 'B' | 'F',
  utmSource: string,
  utmMedium: string
}

export interface AiportModel {
  country: string,
  cities: {
    _id?: string,
    name: string,
    code: string
  }[],
}
