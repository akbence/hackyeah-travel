export interface SessionInModel {
  app_name: string,
  secret_key: string,
  params?: {
    market: string,
    language: string,
  }
}

export interface SessionOutModel {
  access_token: string,
  expires_in: number,
  token_type: string
}
