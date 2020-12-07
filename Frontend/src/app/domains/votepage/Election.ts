import { Party } from './Party';
import { DataService } from '../../services/data.service';

export interface Election{
  name: string;
  parties: Party[];
  id: number;
}
