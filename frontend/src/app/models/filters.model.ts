export interface FilterModel {
  fare: FilterFareModel,
  price: {
    max: number
  },
  departure: {
    min: number,
    max: number
  },
  return: {
    min: number,
    max: number
  },
}

export interface FilterFareModel {
  saver: boolean,
  standard: boolean,
  flex: boolean,
  bizstd: boolean
}
